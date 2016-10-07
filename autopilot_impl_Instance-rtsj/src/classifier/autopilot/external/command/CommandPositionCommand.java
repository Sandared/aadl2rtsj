package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.booleandata.BooleanData;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandPositionCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "altitude"
	 */
	private FloatData altitude = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "longitude"
	 */
	private FloatData longitude = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "latitude"
	 */
	private FloatData latitude = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "loiter"
	 */
	private BooleanData loiter = new BooleanData();

	/**
	 * A member variable to store the value for subcomponent "heading"
	 */
	private FloatData heading = new FloatData();

	//########### Getter/Setter ###########
	/**
	 * @return the value of altitude
	 */
	public FloatData getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude the new value for altitude
	 */
	public void setAltitude(FloatData altitude) {
		this.altitude = altitude;
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
	 * @return the value of loiter
	 */
	public BooleanData getLoiter() {
		return loiter;
	}

	/**
	 * @param loiter the new value for loiter
	 */
	public void setLoiter(BooleanData loiter) {
		this.loiter = loiter;
	}

	/**
	 * @return the value of heading
	 */
	public FloatData getHeading() {
		return heading;
	}

	/**
	 * @param heading the new value for heading
	 */
	public void setHeading(FloatData heading) {
		this.heading = heading;
	}

	@Override
	public CommandPositionCommand deepCopy() {
		CommandPositionCommand result = new CommandPositionCommand();
		result.altitude = altitude.deepCopy();
		result.longitude = longitude.deepCopy();
		result.latitude = latitude.deepCopy();
		result.loiter = loiter.deepCopy();
		result.heading = heading.deepCopy();
		return result;
	}
}
