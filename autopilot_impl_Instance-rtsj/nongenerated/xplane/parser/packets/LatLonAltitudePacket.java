package xplane.parser.packets;

/**
 * X-Plane data packet containing the following fields:
 * 
 * <ul>
 * <li>{@link #latitude}: Latitude [deg]</li>
 * <li>{@link #longitude}: Longitude [deg]</li>
 * <li>{@link #altitudeMsl}: Altitude above MSL [ft]</li>
 * <li>{@link #altitudeAgl}: Altitude above ground level [ft]</li>
 * </ul>
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class LatLonAltitudePacket implements Packet {
	private final float latitude;
	private final float longitude;
	private final float altitudeMsl;
	private final float altitudeAgl;

	public LatLonAltitudePacket(float latitude, float longitude, float altitudeMsl, float altitudeAgl) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitudeMsl = altitudeMsl;
		this.altitudeAgl = altitudeAgl;
	}

	/**
	 * @return Latitude in deg
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @return Longitude in deg
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @return Altitude above MSL in ft
	 */
	public float getAltitudeMsl() {
		return altitudeMsl;
	}

	/**
	 * @return Altitude above ground level in ft
	 */
	public float getAltitudeAgl() {
		return altitudeAgl;
	}

	@Override
	public String toString() {
		return String.format("[lat=%.2f, lon=%.2f, altMsl=%.2f, altAgl=%.2f]", latitude, longitude, altitudeMsl,
				altitudeAgl);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(altitudeAgl);
		result = prime * result + Float.floatToIntBits(altitudeMsl);
		result = prime * result + Float.floatToIntBits(latitude);
		result = prime * result + Float.floatToIntBits(longitude);
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
		final LatLonAltitudePacket other = (LatLonAltitudePacket) obj;
		if (Float.floatToIntBits(altitudeAgl) != Float.floatToIntBits(other.altitudeAgl)) {
			return false;
		}
		if (Float.floatToIntBits(altitudeMsl) != Float.floatToIntBits(other.altitudeMsl)) {
			return false;
		}
		if (Float.floatToIntBits(latitude) != Float.floatToIntBits(other.latitude)) {
			return false;
		}
		if (Float.floatToIntBits(longitude) != Float.floatToIntBits(other.longitude)) {
			return false;
		}
		return true;
	}
}
