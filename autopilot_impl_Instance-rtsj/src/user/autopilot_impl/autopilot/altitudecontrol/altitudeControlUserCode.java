package user.autopilot_impl.autopilot.altitudecontrol;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InOutPort;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.autopilot.external.command.CommandAltitudeCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.request.RequestThrottleRequest;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code addtionalImports
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;
import common.AutopilotHelper;
import common.PIDController;
// End of user code

public class altitudeControlUserCode {

	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(altitudeControlUserCode.class.getName());

	private InPort<InformationPositionInformation> positionIn;
	private OutPort<RequestThrottleRequest> throttleRequestOut;
	private InPort<CommandShutdownCommand> shutdownCommandIn;
	private InPort<CommandAltitudeCommand> commandIn;

	// Start of user code additionalVariables
	private PIDController pid;
	private float currentThrottleSetting = 0.0f;
	private final float integralInit = 77500f;
	// End of user code

	public altitudeControlUserCode(InPort<InformationPositionInformation> positionIn,
			OutPort<RequestThrottleRequest> throttleRequestOut, InPort<CommandShutdownCommand> shutdownCommandIn,
			InPort<CommandAltitudeCommand> commandIn) {
		this.positionIn = positionIn;
		this.throttleRequestOut = throttleRequestOut;
		this.shutdownCommandIn = shutdownCommandIn;
		this.commandIn = commandIn;
		// Start of user code initialization
		pid = new PIDController(AutopilotHelper.getAltitudePidParameters(), integralInit);
		// End of user code
	}

	public void dispatch() {
		// Start of user code dispatch
		// End of user code
	}

	public void start() {
		// Start of user code start
		// End of user code
	}

	public void completion() {
		// Start of user code completion
		// End of user code
	}

	public void compute() {
		// Start of user code compute
		if (positionIn.isDirty()) {
			InformationPositionInformation positionInfo;
			try {
				positionInfo = positionIn.getData();
				if (positionInfo.getLocation() != null
						&& !Float.isNaN(positionInfo.getLocation().getAltitudeMsl().getValue())) {
					final float altitude = positionInfo.getLocation().getAltitudeMsl().getValue();
					currentThrottleSetting = pid.update(altitude);
				}
			} catch (NoDataException e) {
				//log.info("No data on in port");
			}
		}
		if (commandIn.isDirty()) {
			CommandAltitudeCommand command;
			try {
				command = commandIn.getData();
				setTargetAltitude(command.getAltitude().getValue().intValue());
			} catch (NoDataException e) {
				//log.info("No data on in port");
			}
		}

		controlAltitude();
		// End of user code
	}

	// Start of user code helperstuff
	private void setTargetAltitude(int target) {
		if (pid.getSetpoint() == null) {
			//log.info("Received new Targetaltitude: " + target);
			pid.setSetpoint(target, false);
		} else if (pid.getSetpoint() != null && target != pid.getSetpoint()) {
			//log.info("Received new Targetaltitude: " + target);
			pid.setSetpoint(target, false);
		}
	}

	private void controlAltitude() {
		// Send it off
		RequestThrottleRequest throttleRequest = new RequestThrottleRequest();
		throttleRequest.getControllerType().setValue(AutopilotHelper.ALTITUDE_CONTROLLER);
		float test = currentThrottleSetting;
		throttleRequest.getThrottleRequestEngine1().setValue(currentThrottleSetting);
		throttleRequest.getThrottleRequestEngine2().setValue(currentThrottleSetting);
		throttleRequest.getThrottleRequestEngine3().setValue(currentThrottleSetting);
		throttleRequest.getThrottleRequestEngine4().setValue(currentThrottleSetting);
		if (test != currentThrottleSetting)
			System.out.println("different values altitudecontrol");
		throttleRequestOut.setData(throttleRequest);
	}
	// End of user code
}
