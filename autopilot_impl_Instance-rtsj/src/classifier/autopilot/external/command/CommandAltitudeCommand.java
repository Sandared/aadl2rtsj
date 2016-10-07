package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandAltitudeCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "altitude"
	 */
	private FloatData altitude = new FloatData();

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

	@Override
	public CommandAltitudeCommand deepCopy() {
		CommandAltitudeCommand result = new CommandAltitudeCommand();
		result.altitude = altitude.deepCopy();
		return result;
	}
}
