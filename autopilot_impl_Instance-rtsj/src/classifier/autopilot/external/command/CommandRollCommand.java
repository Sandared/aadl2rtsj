package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandRollCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "roll"
	 */
	private FloatData roll = new FloatData();

	//########### Getter/Setter ###########
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

	@Override
	public CommandRollCommand deepCopy() {
		CommandRollCommand result = new CommandRollCommand();
		result.roll = roll.deepCopy();
		return result;
	}
}
