package bus.api;

/**
 * Represents a coordinate on Earth as specified by latitude, longitude, and on
 * optional altitudes above mean sea level and above ground level.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class GeoCoordinate {
	/**
	 * Geographical latitude in deg, positive northwards
	 */
	private final double latitude;

	/**
	 * Geographical longitude in deg, positive eastwards
	 */
	private final double longitude;

	/**
	 * Altitude above mean sea level in ft, optional
	 */
	private Double altitudeMsl = null;

	/**
	 * Altitude above ground level in ft, optional
	 */
	private Double altitudeAgl = null;

	/**
	 * Constructs a new GeoCoordinate consisting of only latitude and longitude.
	 * Altitude information is not available in the returned instance.
	 *
	 * @param latitude  the geographical latitude in deg, positive northwards
	 * @param longitude the geographical longitude in deg, positive eastwards
	 * @throws IllegalArgumentException if the latitude or longitude values are outside their
	 *                                  respective valid ranges (-90°..+90° for latitude,
	 *                                  -180°..+180° for longitude)
	 */
	public GeoCoordinate(double latitude, double longitude) {
		if (Math.abs(latitude) > 90) {
			throw new IllegalArgumentException("Latitude must be between -90 deg and +90 deg, was " + latitude);
		}
		if (Math.abs(longitude) > 180) {
			throw new IllegalArgumentException("Longitude must be between -180 deg and +180 deg, was " + longitude);
		}

		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Constructs a new GeoCoordinate with additional information about the
	 * altitude above mean sea level.
	 *
	 * @param latitude    the geographical latitude in deg, positive northwards
	 * @param longitude   the geographical longitude in deg, positive eastwards
	 * @param altitudeMsl the altitude above mean sea level in ft
	 * @see #GeoCoordinate(double, double)
	 */
	public GeoCoordinate(double latitude, double longitude, double altitudeMsl) {
		this(latitude, longitude);
		this.altitudeMsl = altitudeMsl;
	}

	/**
	 * Constructs a new GeoCoordinate with additional information about the
	 * altitude above mean sea level and above ground level.
	 *
	 * @param latitude    the geographical latitude in deg, positive northwards
	 * @param longitude   the geographical longitude in deg, positive eastwards
	 * @param altitudeMsl the altitude above mean sea level in ft
	 * @param altitudeAgl the altitude above ground level in ft
	 * @see GeoCoordinate#GeoCoordinate(double, double)
	 */
	public GeoCoordinate(double latitude, double longitude, double altitudeMsl, double altitudeAgl) {
		this(latitude, longitude);
		this.altitudeMsl = altitudeMsl;
		this.altitudeAgl = altitudeAgl;
	}

	/**
	 * Returns the latitude in deg stored in this instance. Positive values
	 * indicate northern latitude.
	 *
	 * @return the latitude in deg
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Returns the longitude in deg stored in this instance. Positive values
	 * indicate eastern longitude.
	 *
	 * @return the latitude in deg
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Indicates if this GeoCoordinate contains an altitude above mean sea
	 * level.
	 *
	 * @return true, if an altitude above mean sea level is set in this instance
	 */
	public boolean hasAltitudeMsl() {
		return altitudeMsl != null;
	}

	/**
	 * Indicates if this GeoCoordinate contains an altitude above ground level.
	 *
	 * @return true, if an altitude above ground level is set in this instance
	 */
	public boolean hasAltitudeAgl() {
		return altitudeAgl != null;
	}

	/**
	 * Returns the altitude above mean sea level, if present in this instance.
	 * If the altitude has not been set, an exception is thrown.
	 *
	 * @return the altitude above mean sea level in ft
	 * @throws IllegalStateException if no altitude above mean sea level is set in this instance
	 */
	public double getAltitudeMsl() {
		if (!hasAltitudeMsl()) {
			throw new IllegalStateException(
					"Cannot retrieve altitude above mean sea level from GeoCoordinate without one set.");
		}
		return altitudeMsl;
	}

	/**
	 * Returns the altitude above ground level, if present in this instance. If
	 * the altitude has not been set, an exception is thrown.
	 *
	 * @return the altitude above ground level in ft
	 * @throws IllegalStateException if no altitude above ground level is set in this instance
	 */
	public double getAltitudeAgl() {
		if (!hasAltitudeAgl()) {
			throw new IllegalStateException(
					"Cannot retrieve altitude above ground level from GeoCoordinate without one set.");
		}
		return altitudeAgl;
	}

	private static String formatAngle(Double angle) {
		final double val = angle;
		final int degs = (int) val;
		final int arcmins = (int) ((val - degs) * 60);
		final double arcsecs = (val - degs - arcmins / 60) * 3600;
		return String.format("%d° %d' %.2f", degs, arcmins, arcsecs);
	}

	@Override
	public String toString() {
		return String.format("[lat = %s, lon = %s, alt MSL = %s m, alt AGL = %s m]", formatAngle(latitude),
				formatAngle(longitude), hasAltitudeMsl() ? String.format("%.2f", altitudeMsl) : "---",
				hasAltitudeAgl() ? String.format("%.2f", altitudeAgl) : "---");
	}
}
