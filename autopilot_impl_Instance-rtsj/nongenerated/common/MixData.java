package common;

import classifier.autopilot.external.request.RequestThrottleRequest;

/**
 * Data object that holds altitude, roll, pitch and orientation information for
 * the same time slot
 * 
 * @author Manuel Hoss (manuel.hoss@hotmail.de)
 */
public class MixData {

	private RequestThrottleRequest altitudeControlMessage;
	private RequestThrottleRequest rollControlMessage;
	private RequestThrottleRequest pitchControlMessage;
	private RequestThrottleRequest orientationControlMessage;

	public RequestThrottleRequest getAltitudeControlMessage() {
		return altitudeControlMessage;
	}

	public void setAltitudeControlMessage(RequestThrottleRequest altitudeControlMessage) {
		this.altitudeControlMessage = altitudeControlMessage;
	}

	public RequestThrottleRequest getRollControlMessage() {
		return rollControlMessage;
	}

	public void setRollControlMessage(RequestThrottleRequest rollControlMessage) {
		this.rollControlMessage = rollControlMessage;
	}

	public RequestThrottleRequest getPitchControlMessage() {
		return pitchControlMessage;
	}

	public void setPitchControlMessage(RequestThrottleRequest pitchControlMessage) {
		this.pitchControlMessage = pitchControlMessage;
	}

	public RequestThrottleRequest getOrientationControlMessage() {
		return orientationControlMessage;
	}

	public void setOrientationControlMessage(RequestThrottleRequest orientationControlMessage) {
		this.orientationControlMessage = orientationControlMessage;
	}

	/**
	 * Checks if information for all four messages are set.
	 * 
	 * @return true if all four messages are set to a value
	 */
	public boolean hasAllData() {
		return altitudeControlMessage != null && rollControlMessage != null && pitchControlMessage != null
				&& orientationControlMessage != null;
	}
}