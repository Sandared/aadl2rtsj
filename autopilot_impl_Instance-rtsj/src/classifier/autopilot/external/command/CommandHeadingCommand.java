package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.booleandata.BooleanData;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandHeadingCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "heading"
	 */
	private FloatData heading = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "exit"
	 */
	private BooleanData exit = new BooleanData();

	/**
	 * A member variable to store the value for subcomponent "loiter"
	 */
	private BooleanData loiter = new BooleanData();

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
	 * @return the value of exit
	 */
	public BooleanData getExit() {
		return exit;
	}

	/**
	 * @param exit the new value for exit
	 */
	public void setExit(BooleanData exit) {
		this.exit = exit;
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

	@Override
	public CommandHeadingCommand deepCopy() {
		CommandHeadingCommand result = new CommandHeadingCommand();
		result.heading = heading.deepCopy();
		result.exit = exit.deepCopy();
		result.loiter = loiter.deepCopy();
		return result;
	}
}
