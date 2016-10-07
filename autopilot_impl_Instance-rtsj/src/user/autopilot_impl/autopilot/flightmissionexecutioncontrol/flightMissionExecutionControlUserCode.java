package user.autopilot_impl.autopilot.flightmissionexecutioncontrol;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InOutPort;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code addtionalImports
import missionparser.Mission;
import common.AutopilotHelper;
import missionparser.MissionCommand;
import missionparser.MissionCommandType;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;
// End of user code

public class flightMissionExecutionControlUserCode {

	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(flightMissionExecutionControlUserCode.class.getName());

	private InPort<InformationPositionInformation> positionInformationIn;
	private OutPort<CommandShutdownCommand> shutdownCommandOut;
	private OutPort<CommandPositionCommand> positionCommandOut;
	private OutPort<InformationPositionInformation> positionInformationOut;

	// Start of user code additionalVariables
	private Mission mission;
	private MissionCommand currentCommand;
	private long loiterEND;
	private float landing = Float.NaN;
	public static final double ON_WAYPOINT_ERROR_DEVIATION = 40;
	// End of user code

	public flightMissionExecutionControlUserCode(InPort<InformationPositionInformation> positionInformationIn,
			OutPort<CommandShutdownCommand> shutdownCommandOut, OutPort<CommandPositionCommand> positionCommandOut,
			OutPort<InformationPositionInformation> positionInformationOut) {
		this.positionInformationIn = positionInformationIn;
		this.shutdownCommandOut = shutdownCommandOut;
		this.positionCommandOut = positionCommandOut;
		this.positionInformationOut = positionInformationOut;
		// Start of user code initialization
		mission = new Mission("MissionInfo.waypoints");
		currentCommand = getNextWaypoint();
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
		if (positionInformationIn.isDirty()) {
			InformationPositionInformation positionInformation;
			try {
				positionInformation = positionInformationIn.getData();
				if (positionInformation.getLocation() != null) {
					if (Double.isNaN(landing)) {
						onTheWay(positionInformation);
					} else {
						land(positionInformation);
					}
				}
				//forward position
				positionInformationOut.setData(positionInformation);
			} catch (NoDataException e) {
				//log.info("No data on in port");
			}
		}
		// End of user code
	}

	// Start of user code helperstuff
	private void broadcastSetpointCommand(float altitude, float latitude, float longitude, float heading,
			boolean loiter) {
		CommandPositionCommand pos = new CommandPositionCommand();
		pos.getHeading().setValue(heading);
		pos.getLoiter().setValue(loiter);
		pos.getAltitude().setValue(altitude);
		pos.getLatitude().setValue(latitude);
		pos.getLongitude().setValue(longitude);
		pos.getLoiter().setValue(loiter);
		positionCommandOut.setData(pos);
	}

	private boolean reachedTarget(Mission mission) {
		return mission.isMissionCommandsEmpty();
	}

	private void land(InformationPositionInformation positionInformation) {
		//log.info("LAND");
		// check if we are in landing mode and landed already
		if (!Float.isNaN(landing) && positionInformation.getLocation().getAltitudeMsl().getValue() < 0.) {
			//TODO [Thomas] 
			//			exit(positionInformation);
		} else {
			// check if we are the first time in landing mode - true: set new
			// target altitude - false decrease target altitude
			if (Float.isNaN(landing))
				landing = currentCommand.getAltitude()
						- (float) Math.log(positionInformation.getLocation().getAltitudeMsl().getValue()) / 8;
			else
				landing -= Math.log(positionInformation.getLocation().getAltitudeMsl().getValue()) / 8;
			float altitude = (landing > 1 ? landing : 0);
			broadcastSetpointCommand(altitude, currentCommand.getLatitude(), currentCommand.getLongitude(),
					currentCommand.getSpecificParameter4(), true);
		}
	}

	private boolean shallLoiter(MissionCommand command) {
		if (command.getCommandType() == MissionCommandType.MAV_CMD_NAV_LOITER_TIME) {
			final long current = System.currentTimeMillis();
			// set loiter end time if we are the first time in loiter mode
			if (loiterEND < 0)
				loiterEND = (long) (current + 1000l * (long) command.getSpecificParameter1());
			// check if shall stop loiter
			if (current <= loiterEND)
				return true;
		}
		// set loiter end time do default value
		loiterEND = -1;
		return false;
	}

	private void loiter(InformationPositionInformation positionInformation) {
		//log.info("LOITER: " + currentCommand);
		final double distance = AutopilotHelper.getDistanceToWaypoint(
				positionInformation.getLocation().getLatitude().getValue(),
				positionInformation.getLocation().getLongitude().getValue(), currentCommand.getLatitude(),
				currentCommand.getLongitude());
		//log.info("Distance to waypoint: " + distance + " - current target waypoint: " + currentCommand
		//		+ ": loiter time left " + (loiterEND - System.currentTimeMillis()));
		broadcastSetpointCommand(currentCommand.getAltitude(), currentCommand.getLatitude(),
				currentCommand.getLongitude(), currentCommand.getSpecificParameter4(), true);
	}

	private MissionCommand getNextWaypoint() {
		MissionCommand nextCommand = mission.getNextMissionCommand();
		shallLoiter(nextCommand);
		return nextCommand;
	}

	private void onTheWay(InformationPositionInformation positionInformation) {
		if (isInRange(currentCommand, positionInformation))
			inRange(positionInformation);
		else
			fly(positionInformation);
	}

	private void fly(InformationPositionInformation positionInformation) {
		float heading = (float) AutopilotHelper.getTargetDestination(
				positionInformation.getLocation().getLatitude().getValue(),
				positionInformation.getLocation().getLongitude().getValue(), currentCommand.getLatitude(),
				currentCommand.getLongitude());
		broadcastSetpointCommand(currentCommand.getAltitude(), currentCommand.getLatitude(),
				currentCommand.getLongitude(), heading, false);
		//log.info("FLY: " + currentCommand + "\nHeading: " + heading);
	}

	private boolean isInRange(MissionCommand command, InformationPositionInformation positionInformation) {
		double distance = AutopilotHelper.getDistanceToWaypoint(
				positionInformation.getLocation().getLatitude().getValue(),
				positionInformation.getLocation().getLongitude().getValue(), command.getLatitude(),
				command.getLongitude());
		//log.info("Distance to next waypoint: " + distance + " - current target waypoint: " + command);
		// offset to handle overshoot in transition from fly to loiter
		if (loiterEND > -1)
			distance /= 2.;
		// check if we are in skipping mode and choose reference distance for
		// range
		return distance <= (command.getCommandType() == MissionCommandType.MAV_CMD_NAV_SKIP
				? Math.max(command.getSpecificParameter2(), ON_WAYPOINT_ERROR_DEVIATION) : ON_WAYPOINT_ERROR_DEVIATION);
	}

	private void inRange(InformationPositionInformation positionInformation) {
		if (shallSkip(currentCommand)) {
			currentCommand = getNextWaypoint();
		} else {
			if (isOnWaypoint(currentCommand, positionInformation)) {
				onWaypoint(positionInformation);
			} else {
				fly(positionInformation);
			}
		}
	}

	private void onWaypoint(InformationPositionInformation positionInformation) {
		if (reachedTarget(mission))
			land(positionInformation);
		else if (shallLoiter(currentCommand))
			loiter(positionInformation);
		else
			currentCommand = getNextWaypoint();
	}

	private boolean shallSkip(MissionCommand command) {
		return command.getCommandType() == MissionCommandType.MAV_CMD_NAV_SKIP;
	}

	private boolean isOnWaypoint(MissionCommand command, InformationPositionInformation positionInformation) {
		double distance = AutopilotHelper.getDistanceToWaypoint(
				positionInformation.getLocation().getLatitude().getValue(),
				positionInformation.getLocation().getLongitude().getValue(), command.getLatitude(),
				command.getLongitude());
		// offset to handle overshoot in transition from fly to loiter
		if (loiterEND > -1)
			distance /= 2.;
		return distance <= ON_WAYPOINT_ERROR_DEVIATION;
	}
	// End of user code
}
