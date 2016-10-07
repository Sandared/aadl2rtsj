package classifier.autopilot.external.request;

//########## Classifier Imports ##########
import classifier.autopilot.external.request.Request;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata.FloatData;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.stringdata.StringData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.request.Request;

public class RequestThrottleRequest implements Request {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "throttleRequestEngine1"
	 */
	private FloatData throttleRequestEngine1 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "throttleRequestEngine2"
	 */
	private FloatData throttleRequestEngine2 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "throttleRequestEngine3"
	 */
	private FloatData throttleRequestEngine3 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "throttleRequestEngine4"
	 */
	private FloatData throttleRequestEngine4 = new FloatData();

	/**
	 * A member variable to store the value for subcomponent "controllerType"
	 */
	private StringData controllerType = new StringData();

	//########### Getter/Setter ###########
	/**
	 * @return the value of throttleRequestEngine1
	 */
	public FloatData getThrottleRequestEngine1() {
		return throttleRequestEngine1;
	}

	/**
	 * @param throttleRequestEngine1 the new value for throttleRequestEngine1
	 */
	public void setThrottleRequestEngine1(FloatData throttleRequestEngine1) {
		this.throttleRequestEngine1 = throttleRequestEngine1;
	}

	/**
	 * @return the value of throttleRequestEngine2
	 */
	public FloatData getThrottleRequestEngine2() {
		return throttleRequestEngine2;
	}

	/**
	 * @param throttleRequestEngine2 the new value for throttleRequestEngine2
	 */
	public void setThrottleRequestEngine2(FloatData throttleRequestEngine2) {
		this.throttleRequestEngine2 = throttleRequestEngine2;
	}

	/**
	 * @return the value of throttleRequestEngine3
	 */
	public FloatData getThrottleRequestEngine3() {
		return throttleRequestEngine3;
	}

	/**
	 * @param throttleRequestEngine3 the new value for throttleRequestEngine3
	 */
	public void setThrottleRequestEngine3(FloatData throttleRequestEngine3) {
		this.throttleRequestEngine3 = throttleRequestEngine3;
	}

	/**
	 * @return the value of throttleRequestEngine4
	 */
	public FloatData getThrottleRequestEngine4() {
		return throttleRequestEngine4;
	}

	/**
	 * @param throttleRequestEngine4 the new value for throttleRequestEngine4
	 */
	public void setThrottleRequestEngine4(FloatData throttleRequestEngine4) {
		this.throttleRequestEngine4 = throttleRequestEngine4;
	}

	/**
	 * @return the value of controllerType
	 */
	public StringData getControllerType() {
		return controllerType;
	}

	/**
	 * @param controllerType the new value for controllerType
	 */
	public void setControllerType(StringData controllerType) {
		this.controllerType = controllerType;
	}

	@Override
	public RequestThrottleRequest deepCopy() {
		RequestThrottleRequest result = new RequestThrottleRequest();
		result.throttleRequestEngine1 = throttleRequestEngine1.deepCopy();
		result.throttleRequestEngine2 = throttleRequestEngine2.deepCopy();
		result.throttleRequestEngine3 = throttleRequestEngine3.deepCopy();
		result.throttleRequestEngine4 = throttleRequestEngine4.deepCopy();
		result.controllerType = controllerType.deepCopy();
		return result;
	}
}
