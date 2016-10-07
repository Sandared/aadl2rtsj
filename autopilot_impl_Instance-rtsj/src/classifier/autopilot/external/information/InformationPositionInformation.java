package classifier.autopilot.external.information;

//########## Classifier Imports ##########
import classifier.autopilot.external.geocoordinate.GeoCoordinateimpl;
import classifier.autopilot.external.information.Information;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.booleandata.BooleanData;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.information.Information;

public class InformationPositionInformation implements Information {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "heading"
	 */
	private FloatData heading = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "pitch"
	 */
	private FloatData pitch = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "roll"
	 */
	private FloatData roll = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "loiter"
	 */
	private BooleanData loiter = new BooleanData();

	/**
	 * A member variable to store the value for subcomponent "location"
	 */
	private GeoCoordinateimpl location = new GeoCoordinateimpl();

	//########### Getter/Setter ###########
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

	/**
	 * @return the value of pitch
	 */
	public FloatData getPitch() {
		return pitch;
	}

	/**
	 * @param pitch the new value for pitch
	 */
	public void setPitch(FloatData pitch) {
		this.pitch = pitch;
	}

	/**
	 * @return the value of roll
	 */
	public FloatData getRoll() {
		return roll;
	}

	/**
	 * @param roll the new value for roll
	 */
	public void setRoll(FloatData roll) {
		this.roll = roll;
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
	 * @return the value of location
	 */
	public GeoCoordinateimpl getLocation() {
		return location;
	}

	/**
	 * @param location the new value for location
	 */
	public void setLocation(GeoCoordinateimpl location) {
		this.location = location;
	}

	@Override
	public InformationPositionInformation deepCopy() {
		InformationPositionInformation result = new InformationPositionInformation();
		result.heading = heading.deepCopy();
		result.pitch = pitch.deepCopy();
		result.roll = roll.deepCopy();
		result.loiter = loiter.deepCopy();
		result.location = location.deepCopy();
		return result;
	}
}
