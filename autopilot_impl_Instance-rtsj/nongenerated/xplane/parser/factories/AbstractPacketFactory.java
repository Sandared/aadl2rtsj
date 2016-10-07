package xplane.parser.factories;

import java.nio.ByteBuffer;

import xplane.parser.XPlanePacketConsumer;
import xplane.parser.exceptions.MalformedPacketException;

/**
 * An abstract implementation of a parser for a single X-Plane data packet type.
 * <p/>
 * Implementations are expected to override the {
 * {@link #parseInternal(ByteBuffer, XPlanePacketConsumer)} method according to
 * the internal structure of the data packet. They need not handle verification
 * of the packet header, this task is carried out by this abstract base class.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public abstract class AbstractPacketFactory implements PacketFactory {

	private final void validateHeader(ByteBuffer bytes) throws MalformedPacketException {
		final int type = bytes.getInt();
		if (type != getPacketType()) {
			throw new MalformedPacketException(
					String.format("Expected packet type %d, got %d instead.", getPacketType(), type));
		}
	}

	@Override
	public abstract int getPacketType();

	@Override
	public void parse(ByteBuffer bytes, XPlanePacketConsumer consumer) throws MalformedPacketException {
		validateHeader(bytes);

		// Delegate the actual parsing to the subclass implementation
		parseInternal(bytes, consumer);
	}

	/**
	 * Parse the actual packet contents.
	 * <p/>
	 * The implementation is responsible for invoking the callback method on the
	 * consumer.
	 * 
	 * @param bytes
	 *            the binary X-Plane data packet (header already verified)
	 * @param consumer
	 *            the consumer for the extracted information
	 * @throws MalformedPacketException
	 *             if the packet cannot be parsed successfully
	 */
	protected abstract void parseInternal(ByteBuffer bytes, XPlanePacketConsumer consumer)
			throws MalformedPacketException;
}
