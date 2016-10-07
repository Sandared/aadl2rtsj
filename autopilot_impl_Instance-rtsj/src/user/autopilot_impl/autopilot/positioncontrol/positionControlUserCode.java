package user.autopilot_impl.autopilot.positioncontrol;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InOutPort;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandAltitudeCommand;
import classifier.autopilot.external.command.CommandHeadingCommand;
import classifier.autopilot.external.command.CommandPitchCommand;
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandRollCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code addtionalImports
import common.PIDController;
import common.AutopilotHelper;
import common.MyCommands;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;
// End of user code

public class positionControlUserCode {

	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(positionControlUserCode.class.getName());

	private InPort<InformationPositionInformation> positionInformationIn;
	private InPort<CommandPositionCommand> positionCommandIn;
	private InPort<CommandShutdownCommand> shutdownCommandIn;
	private OutPort<CommandAltitudeCommand> altitudeCommandOut;
	private OutPort<CommandPitchCommand> pitchCommandOut;
	private OutPort<CommandRollCommand> rollCommandOut;
	private OutPort<CommandHeadingCommand> headingCommandOut;
	private OutPort<InformationPositionInformation> positionInformationOut;
	private OutPort<CommandShutdownCommand> shutdownCommandOut;

	// Start of user code additionalVariables
	private PIDController pid;

	private float targetAltitude = 0.0f;
	private float targetLatitude = 0.0f;
	private float targetLongitude = 0.0f;
	private float targetSpeed = 0.0f;
	private float targetHeading = 0.0f;

	private float heading = 0.0f;
	private float distance = 0.0f;
	private boolean loiter = false;

	private MyCommands commands = new MyCommands();
	// End of user code

	public positionControlUserCode(InPort<InformationPositionInformation> positionInformationIn,
			InPort<CommandPositionCommand> positionCommandIn, InPort<CommandShutdownCommand> shutdownCommandIn,
			OutPort<CommandAltitudeCommand> altitudeCommandOut, OutPort<CommandPitchCommand> pitchCommandOut,
			OutPort<CommandRollCommand> rollCommandOut, OutPort<CommandHeadingCommand> headingCommandOut,
			OutPort<InformationPositionInformation> positionInformationOut,
			OutPort<CommandShutdownCommand> shutdownCommandOut) {
		this.positionInformationIn = positionInformationIn;
		this.positionCommandIn = positionCommandIn;
		this.shutdownCommandIn = shutdownCommandIn;
		this.altitudeCommandOut = altitudeCommandOut;
		this.pitchCommandOut = pitchCommandOut;
		this.rollCommandOut = rollCommandOut;
		this.headingCommandOut = headingCommandOut;
		this.positionInformationOut = positionInformationOut;
		this.shutdownCommandOut = shutdownCommandOut;
		// Start of user code initialization
		pid = new PIDController(AutopilotHelper.getPositionPidParameters());
		//		commands.start();
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
		pid.setSetpoint(0);

		if (positionCommandIn.isDirty()) {
			CommandPositionCommand command;
			try {
				command = positionCommandIn.getData();
				targetAltitude = command.getAltitude().getValue();
				targetLatitude = command.getLatitude().getValue();
				targetLongitude = command.getLongitude().getValue();
				targetHeading = command.getHeading().getValue();
				loiter = command.getLoiter().getValue();
			} catch (NoDataException e) {
				//				log.info("No data on in port");
			}

		}
		if (positionInformationIn.isDirty()) {
			InformationPositionInformation positionInfo;
			try {
				positionInfo = positionInformationIn.getData();
				if (positionInfo.getLocation() != null) {
					distance = (float) AutopilotHelper.getDistanceToWaypoint(targetLatitude, targetLongitude,
							positionInfo.getLocation().getLatitude().getValue(),
							positionInfo.getLocation().getLongitude().getValue());
					targetSpeed = (float) pid.update(distance);
					heading = positionInfo.getHeading().getValue();
				}
				//forward position
				positionInformationOut.setData(positionInfo);
			} catch (NoDataException e) {
				//				log.info("No data on in port");
			}
		}
		controlPosition();
		// End of user code
	}

	// Start of user code helperstuff
	private float controlPitch() {
		if (loiter)
			// Calculate the portion for roll and pitch according to the heading
			// angle
			return (float) (Math.cos(Math.toRadians(heading)) * targetSpeed);
		else
			return targetSpeed;
	}

	private float controlRoll() {
		if (loiter)
			// Calculate the portion for roll and pitch according to the heading
			// angle
			return (float) (-Math.sin(Math.toRadians(heading)) * targetSpeed);
		else
			return 0;
	}

	private void controlPosition() {

		// Send it off
		CommandAltitudeCommand altitudeCmd = new CommandAltitudeCommand();
		altitudeCmd.getAltitude().setValue(targetAltitude);
		//		altitudeCmd.getAltitude().setValue(commands.getAltitude());
		altitudeCommandOut.setData(altitudeCmd);

		CommandPitchCommand pitchCmd = new CommandPitchCommand();
		pitchCmd.getPitch().setValue(controlPitch());
		//		pitchCmd.getPitch().setValue(commands.getPitch());
		pitchCommandOut.setData(pitchCmd);

		CommandRollCommand rollCmd = new CommandRollCommand();
		rollCmd.getRoll().setValue(controlRoll());
		//		rollCmd.getRoll().setValue(commands.getRoll());
		rollCommandOut.setData(rollCmd);

		CommandHeadingCommand headingCmd = new CommandHeadingCommand();
		headingCmd.getHeading().setValue(targetHeading);
		headingCmd.getLoiter().setValue(loiter);
		//		headingCmd.getHeading().setValue(commands.getHeading());
		//		headingCmd.getLoiter().setValue(commands.isLoiter());
		headingCommandOut.setData(headingCmd);
	}
	// End of user code
}
