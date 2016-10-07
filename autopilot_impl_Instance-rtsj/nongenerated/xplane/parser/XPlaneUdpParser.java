package xplane.parser;

import java.nio.ByteBuffer;

import xplane.parser.exceptions.MalformedPacketException;
import xplane.parser.exceptions.UnknownPacketTypeException;
import xplane.parser.factories.PacketFactory;

/**
 * This interface describes the basic operations to parse the X-Plane UDP data
 * protocol.
 * <p/>
 * Parsers for individual packet types can be registered and removed via the
 * {@link #registerPacketFactory(PacketFactory)} and
 * {@link #unregisterPacketFactory(PacketFactory)} methods.
 * <p/>
 * The actual parser implementations will use the callbacks implemented by a
 * packet consumer, which can be registered and removed with the
 * {@link #bindConsumer(XPlanePacketConsumer)} and
 * {@link #unbindConusmer(XPlanePacketConsumer)} methods.
 * <p/>
 * Alternatively, both parser implementations and the consumer can be injected
 * automatically as OSGi services, if they implement the respective interfaces.
 * <p/>
 * A detailed documentation of the X-Plane UDP data protocol can be found <a
 * href="http://b58.svglobe.com/data.html">here</a>.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public interface XPlaneUdpParser {
	/**
	 * Registers a new packet parser implementation.
	 *
	 * @param factory
	 */
	void registerPacketFactory(PacketFactory factory);

	/**
	 * Registers a consumer for parsed packets.
	 *
	 * @param consumer
	 */
	void bindConsumer(XPlanePacketConsumer consumer);

	/**
	 * Parses an single group within a larger UDP message.
	 *
	 * @param buffer
	 * @return true if the group contains actual data and could be parsed
	 * successfully, false if it is a dummy group.
	 * @throws MalformedPacketException   if the group could not be parsed correctly.
	 * @throws UnknownPacketTypeException if no parser has been registered for the type of the supplied
	 *                                    group
	 */
	boolean parse(ByteBuffer buffer) throws MalformedPacketException, UnknownPacketTypeException;

	/**
	 * Parses an entire UDP data packet.
	 *
	 * @param message
	 * @throws MalformedPacketException   if any of the packet's groups are malformed (see
	 *                                    {@link #parse(ByteBuffer)}) or the packet header is invalid.
	 * @throws UnknownPacketTypeException if the packet contains a data group for which no parser
	 *                                    implementation has been registered.
	 */
	void parseMessage(ByteBuffer message) throws MalformedPacketException, UnknownPacketTypeException;
}
