package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandThrottleCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "throttleEngine1"
	 */
	private FloatData throttleEngine1 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "throttleEngine2"
	 */
	private FloatData throttleEngine2 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "throttleEngine3"
	 */
	private FloatData throttleEngine3 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "throttleEngine4"
	 */
	private FloatData throttleEngine4 = new FloatData();

	//########### Getter/Setter ###########
	/**
	 * @return the value of throttleEngine1
	 */
	public FloatData getThrottleEngine1() {
		return throttleEngine1;
	}

	/**
	 * @param throttleEngine1 the new value for throttleEngine1
	 */
	public void setThrottleEngine1(FloatData throttleEngine1) {
		this.throttleEngine1 = throttleEngine1;
	}

	/**
	 * @return the value of throttleEngine2
	 */
	public FloatData getThrottleEngine2() {
		return throttleEngine2;
	}

	/**
	 * @param throttleEngine2 the new value for throttleEngine2
	 */
	public void setThrottleEngine2(FloatData throttleEngine2) {
		this.throttleEngine2 = throttleEngine2;
	}

	/**
	 * @return the value of throttleEngine3
	 */
	public FloatData getThrottleEngine3() {
		return throttleEngine3;
	}

	/**
	 * @param throttleEngine3 the new value for throttleEngine3
	 */
	public void setThrottleEngine3(FloatData throttleEngine3) {
		this.throttleEngine3 = throttleEngine3;
	}

	/**
	 * @return the value of throttleEngine4
	 */
	public FloatData getThrottleEngine4() {
		return throttleEngine4;
	}

	/**
	 * @param throttleEngine4 the new value for throttleEngine4
	 */
	public void setThrottleEngine4(FloatData throttleEngine4) {
		this.throttleEngine4 = throttleEngine4;
	}

	@Override
	public CommandThrottleCommand deepCopy() {
		CommandThrottleCommand result = new CommandThrottleCommand();
		result.throttleEngine1 = throttleEngine1.deepCopy();
		result.throttleEngine2 = throttleEngine2.deepCopy();
		result.throttleEngine3 = throttleEngine3.deepCopy();
		result.throttleEngine4 = throttleEngine4.deepCopy();
		return result;
	}
}
