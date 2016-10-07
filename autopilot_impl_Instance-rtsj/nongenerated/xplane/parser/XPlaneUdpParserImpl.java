package xplane.parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import xplane.parser.exceptions.MalformedPacketException;
import xplane.parser.exceptions.UnknownPacketTypeException;
import xplane.parser.factories.PacketFactory;

/**
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class XPlaneUdpParserImpl implements XPlaneUdpParser {
	/**
	 * X-Plane UDP packets consist of multiple groups, each being 32 bytes in
	 * length
	 */
	private static final int GROUP_PAYLOAD_SIZE = 32;

	/**
	 * X-Plane UDP packet groups are identified by a single float, 4 bytes in
	 * length
	 */
	private static final int GROUP_HEADER_SIZE = 4;

	private static final int GROUP_SIZE = GROUP_HEADER_SIZE + GROUP_PAYLOAD_SIZE;

	/** X-Plane UDP packets start with this fixed identifier */
	private static final String PACKET_HEADER = "DATA";
	private static final int PACKET_HEADER_LENGTH = PACKET_HEADER.length();

	private static final Map<Integer, PacketFactory> factories = new TreeMap<Integer, PacketFactory>();
	private static final Logger log = Logger.getLogger(XPlaneUdpParserImpl.class.getName());

	private static final Charset UTF8 = Charset.forName("UTF8");

	private XPlanePacketConsumer consumer;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * simulation.parser.XPlaneUdpParser#registerPacketFactory(simulation.parser
	 * .factories.AbstractPacketFactory)
	 */
	@Override
	public synchronized void registerPacketFactory(PacketFactory factory) {
		if (factories.containsKey(factory.getPacketType())) {
			throw new IllegalArgumentException(
					String.format("Packet factory for packet type %d already registered.", factory.getPacketType()));
		}

		factories.put(factory.getPacketType(), factory);
		log.info("Registered packet factory " + factory.getClass().getSimpleName() + " for packet type "
				+ factory.getPacketType());
	}

	/*
	 * (non-Javadoc)l
	 *
	 * @see simulation.parser.XPlaneUdpParser#bindConsumer(simulation.parser.
	 * XPlanePacketConsumer)
	 */
	@Override
	public void bindConsumer(XPlanePacketConsumer consumer) {
		log.info("Consumer " + consumer);
		this.consumer = consumer;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see simulation.parser.XPlaneUdpParser#parse(java.nio.ByteBuffer)
	 */
	@Override
	public boolean parse(ByteBuffer buffer) throws MalformedPacketException, UnknownPacketTypeException {
		final int packetType = buffer.getInt();
		if (packetType == 0) {
			return false;
		}

		if (factories.containsKey(packetType)) {
			final PacketFactory factory = factories.get(packetType);
			buffer.rewind();
			factory.parse(buffer, consumer);
			return true;
		} else {
			throw new UnknownPacketTypeException("No parser factory registered for packet type " + packetType);
		}
	}

	@Override
	public synchronized void parseMessage(ByteBuffer message)
			throws MalformedPacketException, UnknownPacketTypeException {
		if (consumer == null) {
			return;
		}

		message.rewind();

		// Verify the fixed packet header to make sure we are dealing with a
		// valid message
		final String hdr = UTF8.decode((ByteBuffer) message.slice().limit(PACKET_HEADER_LENGTH)).toString();

		if (!PACKET_HEADER.equals(hdr)) {
			throw new MalformedPacketException(
					String.format("Received packet header invalid, got '%s' instead of '%s'", hdr, PACKET_HEADER));
		}

		message.position(PACKET_HEADER_LENGTH + 1);
		while (message.remaining() >= GROUP_SIZE) {
			/*
			 * Save current position, so we can return later and skip over data
			 * portion of this group easily
			 */
			message.mark();

			int limit = GROUP_SIZE;
			final ByteBuffer subPacket = message.slice();
			if (limit > subPacket.remaining()) {
				limit = subPacket.remaining();
			}
			subPacket.order(ByteOrder.LITTLE_ENDIAN);
			subPacket.limit(limit);

			final boolean success = parse(subPacket);

			/*
			 * X-Plane pads the message with dummy packets, so we can safely
			 * stop parsing the rest of the packet after encountering the first
			 * of these.
			 */
			if (!success) {
				break;
			}

			/*
			 * Jump back to the mark and skip the entire data section of this
			 * group
			 */
			message.reset();

			if (message.remaining() >= GROUP_SIZE) {
				message.position(message.position() + GROUP_SIZE);
			}
		}
	}
}
