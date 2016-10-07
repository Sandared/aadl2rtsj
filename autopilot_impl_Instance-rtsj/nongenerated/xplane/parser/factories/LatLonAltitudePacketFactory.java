package xplane.parser.factories;

import java.nio.ByteBuffer;

import xplane.parser.XPlanePacketConsumer;
import xplane.parser.exceptions.MalformedPacketException;
import xplane.parser.packets.LatLonAltitudePacket;

/**
 * Parser implementation for the {@link LatLonAltitudePacket} data packet.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class LatLonAltitudePacketFactory extends AbstractPacketFactory {
	@Override
	public int getPacketType() {
		return 20;
	}

	@Override
	protected void parseInternal(ByteBuffer bytes, XPlanePacketConsumer consumer) throws MalformedPacketException {
		final float lat = bytes.getFloat();
		final float lon = bytes.getFloat();
		final float altMsl = bytes.getFloat();
		final float altAgl = bytes.getFloat();
		final LatLonAltitudePacket packet = new LatLonAltitudePacket(lat, lon, altMsl, altAgl);
		consumer.onPacket(packet);
	}
}
