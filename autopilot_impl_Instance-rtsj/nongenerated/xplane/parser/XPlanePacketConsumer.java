package xplane.parser;

import xplane.parser.packets.*;

/**
 * A consumer for parsed X-Plane data packets.
 * 
 * The method structure follows the X-Plane data format.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public interface XPlanePacketConsumer {
	/**
	 * Consumer method for a packet containing latitude, longitude, and altitude
	 * information
	 * 
	 * @param packet
	 *            the parsed data packet
	 */
	void onPacket(LatLonAltitudePacket packet);

	/**
	 * Consumer method for a packet containing angular orientation information
	 * 
	 * @param packet
	 *            the parsed data packet
	 */
	void onPacket(AngularOrientationPacket packet);
}