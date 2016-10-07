package xplane.parser.factories;

import java.nio.ByteBuffer;

import xplane.parser.XPlanePacketConsumer;
import xplane.parser.exceptions.MalformedPacketException;
import xplane.parser.packets.AngularOrientationPacket;

/**
 * Parser implementation for the {@link AngularOrientationPacket} data packet.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class AngularOrientationPacketFactory extends AbstractPacketFactory {
	@Override
	public int getPacketType() {
		return 17;
	}

	@Override
	protected void parseInternal(ByteBuffer bytes, XPlanePacketConsumer consumer) throws MalformedPacketException {
		final float pitch = bytes.getFloat();
		final float roll = bytes.getFloat();
		final float trueHdg = bytes.getFloat();
		final float magHdg = bytes.getFloat();

		final AngularOrientationPacket packet = new AngularOrientationPacket(pitch, roll, trueHdg, magHdg);

		consumer.onPacket(packet);
	}
}
