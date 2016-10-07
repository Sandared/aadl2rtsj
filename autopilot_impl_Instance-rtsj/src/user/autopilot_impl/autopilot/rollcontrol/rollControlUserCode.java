package user.autopilot_impl.autopilot.rollcontrol;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InOutPort;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.autopilot.external.command.CommandRollCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.request.RequestThrottleRequest;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code addtionalImports
import common.PIDController;
import common.AutopilotHelper;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;
// End of user code

public class rollControlUserCode {

	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(rollControlUserCode.class.getName());

	private InPort<InformationPositionInformation> positionIn;
	private OutPort<RequestThrottleRequest> throttleRequestOut;
	private InPort<CommandShutdownCommand> shutdownCommandIn;
	private InPort<CommandRollCommand> commandIn;

	// Start of user code additionalVariables
	private PIDController pid;
	private float currentThrottleSetting = 0.0f;
	// End of user code

	public rollControlUserCode(InPort<InformationPositionInformation> positionIn,
			OutPort<RequestThrottleRequest> throttleRequestOut, InPort<CommandShutdownCommand> shutdownCommandIn,
			InPort<CommandRollCommand> commandIn) {
		this.positionIn = positionIn;
		this.throttleRequestOut = throttleRequestOut;
		this.shutdownCommandIn = shutdownCommandIn;
		this.commandIn = commandIn;
		// Start of user code initialization
		pid = new PIDController(AutopilotHelper.getRollPidParameters());
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
				final float roll = positionInfo.getRoll().getValue();
				currentThrottleSetting = pid.update(roll);
			} catch (NoDataException e) {
				//log.info("No data on in port");
			}
		}

		if (commandIn.isDirty()) {
			CommandRollCommand command;
			try {
				command = commandIn.getData();
				setTargetRoll(command.getRoll().getValue());
			} catch (NoDataException e) {
				//log.info("No data on in port");
			}
		}
		controlRoll();
		// End of user code
	}

	// Start of user code helperstuff
	private void setTargetRoll(float target) {
		if (pid.getSetpoint() == null) {
			//log.info("Received new TargetRoll: " + target);
			pid.setSetpoint(target);
		} else if (pid.getSetpoint() != null && Math.abs(target - pid.getSetpoint()) > 1e-5) {
			//log.info("Received new TargetRoll: " + target);
			pid.setSetpoint(target);
		}
	}

	private void controlRoll() {
		// If we want to change the roll of a quadrocopter we have to
		// accelerate one side of throttles and decelerate the other.
		RequestThrottleRequest throttleRequest = new RequestThrottleRequest();
		throttleRequest.getControllerType().setValue(AutopilotHelper.ROLL_CONTROLER);
		throttleRequest.getThrottleRequestEngine1().setValue(currentThrottleSetting);
		throttleRequest.getThrottleRequestEngine2().setValue(-currentThrottleSetting);
		throttleRequest.getThrottleRequestEngine3().setValue(-currentThrottleSetting);
		throttleRequest.getThrottleRequestEngine4().setValue(currentThrottleSetting);
		// Send it off
		throttleRequestOut.setData(throttleRequest);
	}
	// End of user code
}
