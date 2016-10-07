package xplane.parser.packets;

/**
 * X-Plane data packet containing the following fields:
 * 
 * <ul>
 * <li>{@link #pitch}: Pitch angle [deg]</li>
 * <li>{@link #roll}: Roll angle [deg]</li>
 * <li>{@link #trueHeading}: True heading [deg]</li>
 * <li>{@link #magHeading}: Magnetic heading [deg]</li>
 * </ul>
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class AngularOrientationPacket implements Packet {
	final float pitch;
	final float roll;
	final float trueHeading;
	final float magHeading;

	public AngularOrientationPacket(float pitch, float roll, float trueHeading, float magHeading) {
		this.pitch = pitch;
		this.roll = roll;
		this.trueHeading = trueHeading;
		this.magHeading = magHeading;
	}

	/**
	 * @return the pitch angle in deg
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return the roll angle in deg
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * @return the true heading in deg
	 */
	public float getTrueHeading() {
		return trueHeading;
	}

	/**
	 * @return the magnetic heading in deg
	 */
	public float getMagHeading() {
		return magHeading;
	}

	@Override
	public String toString() {
		return String.format("[roll=%.2f, pitch=%.2f, true hdg=%.2f, mag hdg=%.2f]", roll, pitch, trueHeading,
				magHeading);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(magHeading);
		result = prime * result + Float.floatToIntBits(pitch);
		result = prime * result + Float.floatToIntBits(roll);
		result = prime * result + Float.floatToIntBits(trueHeading);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AngularOrientationPacket other = (AngularOrientationPacket) obj;
		if (Float.floatToIntBits(magHeading) != Float.floatToIntBits(other.magHeading)) {
			return false;
		}
		if (Float.floatToIntBits(pitch) != Float.floatToIntBits(other.pitch)) {
			return false;
		}
		if (Float.floatToIntBits(roll) != Float.floatToIntBits(other.roll)) {
			return false;
		}
		if (Float.floatToIntBits(trueHeading) != Float.floatToIntBits(other.trueHeading)) {
			return false;
		}
		return true;
	}
}
