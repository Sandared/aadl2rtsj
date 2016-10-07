package xplane.parser.factories;

import java.nio.ByteBuffer;

import xplane.parser.XPlanePacketConsumer;
import xplane.parser.exceptions.MalformedPacketException;

/**
 * A parser for X-Plane data packets.
 * <p/>
 * Takes one specific type of X-Plane binary data packet and extracts its
 * encoded information. The extracted information is then passed to a consumer
 * via a callback method.
 * <p/>
 * A list of all packets type can be found within X-Plane on the data output
 * screen (Settings -> Data Input & Output).
 * <p/>
 * The <a href=
 * "http://www.x-plane.com/manuals/desktop/#datainputandoutputfromx-plane">X-
 * Plane manual</a> describes the individual fields within each data packet
 * type.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public interface PacketFactory {
	/**
	 * @return the numeric X-Plane packet type accepted by this parser
	 */
	int getPacketType();

	/**
	 * Parse a binary X-Plane data packet and invoke the consumer's callback
	 * method with the extracted information.
	 * 
	 * @param bytes
	 *            the buffer containing the binary data packet
	 * @param consumer
	 *            the consumer that should be invoked upon successfully parsing
	 *            the packet
	 * @throws MalformedPacketException
	 *             if the packet cannot be parsed correctly
	 */
	void parse(ByteBuffer bytes, XPlanePacketConsumer consumer) throws MalformedPacketException;

}