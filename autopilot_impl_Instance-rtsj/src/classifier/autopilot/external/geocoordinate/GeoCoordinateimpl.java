package classifier.autopilot.external.geocoordinate;

//########## Classifier Imports ##########
import classifier.autopilot.external.geocoordinate.GeoCoordinate;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.geocoordinate.GeoCoordinate;

public class GeoCoordinateimpl implements GeoCoordinate {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "latitude"
	 */
	private FloatData latitude = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "longitude"
	 */
	private FloatData longitude = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "altitudeMsl"
	 */
	private FloatData altitudeMsl = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "altitudeAgl"
	 */
	private FloatData altitudeAgl = new FloatData();

	//########### Getter/Setter ###########
	/**
	 * @return the value of latitude
	 */
	public FloatData getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the new value for latitude
	 */
	public void setLatitude(FloatData latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the value of longitude
	 */
	public FloatData getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the new value for longitude
	 */
	public void setLongitude(FloatData longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the value of altitudeMsl
	 */
	public FloatData getAltitudeMsl() {
		return altitudeMsl;
	}

	/**
	 * @param altitudeMsl the new value for altitudeMsl
	 */
	public void setAltitudeMsl(FloatData altitudeMsl) {
		this.altitudeMsl = altitudeMsl;
	}

	/**
	 * @return the value of altitudeAgl
	 */
	public FloatData getAltitudeAgl() {
		return altitudeAgl;
	}

	/**
	 * @param altitudeAgl the new value for altitudeAgl
	 */
	public void setAltitudeAgl(FloatData altitudeAgl) {
		this.altitudeAgl = altitudeAgl;
	}

	@Override
	public GeoCoordinateimpl deepCopy() {
		GeoCoordinateimpl result = new GeoCoordinateimpl();
		result.latitude = latitude.deepCopy();
		result.longitude = longitude.deepCopy();
		result.altitudeMsl = altitudeMsl.deepCopy();
		result.altitudeAgl = altitudeAgl.deepCopy();
		return result;
	}
}
