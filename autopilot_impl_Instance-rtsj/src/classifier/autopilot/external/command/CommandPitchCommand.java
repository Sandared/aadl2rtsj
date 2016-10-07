package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandPitchCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "pitch"
	 */
	private FloatData pitch = new FloatData();

	//########### Getter/Setter ###########
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

	@Override
	public CommandPitchCommand deepCopy() {
		CommandPitchCommand result = new CommandPitchCommand();
		result.pitch = pitch.deepCopy();
		return result;
	}
}
