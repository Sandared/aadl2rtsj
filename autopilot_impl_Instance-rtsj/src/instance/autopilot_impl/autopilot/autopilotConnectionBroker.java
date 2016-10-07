package instance.autopilot_impl.autopilot;

import java.util.Map;
import java.util.HashMap;

//########## Classifier Imports ##########
import classifier.autopilot.external.altitudecontrol.AltitudeControlimpl;
import classifier.autopilot.external.command.CommandAltitudeCommand;
import classifier.autopilot.external.command.CommandHeadingCommand;
import classifier.autopilot.external.command.CommandPitchCommand;
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandRollCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.datalogger.DataLoggerimpl;
import classifier.autopilot.external.flightmissionexecutioncontrol.FlightMissionExecutionControlimpl;
import classifier.autopilot.external.headingcontrol.HeadingControlimpl;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.mixthrottlescontrol.MixThrottlesControlimpl;
import classifier.autopilot.external.pitchcontrol.PitchControlimpl;
import classifier.autopilot.external.positioncontrol.PositionControlimpl;
import classifier.autopilot.external.request.RequestThrottleRequest;
import classifier.autopilot.external.rollcontrol.RollControlimpl;
import classifier.autopilot.external.autopilotprocess.autopilotProcessimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.BasicConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

/**
 * The standard ConnectionBroker to be used by components with inner connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class autopilotConnectionBroker extends BasicConnectionBroker {

	/**
	 * A logger for logging errors/warnings/etc.
	 */
	private static final Logger log = Logger.getLogger(autopilotConnectionBroker.class.getName());

	/**
	 * A map of synchronisation Objects. For each subcomponent that is the ultimative source of a connection</br>
	 * where a syncObject is needed (immediate connection), a syncObject is created lazily and stored in this Map.
	 */
	private Map<String, Object> syncObjects = new HashMap<String, Object>();

	/**
	 * Broker to be used when retrieving syncObjects from 'higher' nested components
	 */
	private ConnectionBroker parentBroker;

	/**
	 * Component whose out ports are to be used when forwarding connections to 'higher' nested components
	 */
	private autopilotProcessimpl component;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private RollControlimpl rollControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private MixThrottlesControlimpl mixThrottlesControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private PitchControlimpl pitchControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private FlightMissionExecutionControlimpl flightMissionExecutionControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private PositionControlimpl positionControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private AltitudeControlimpl altitudeControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private HeadingControlimpl headingControl;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private DataLoggerimpl dataLogger;

	public autopilotConnectionBroker(ConnectionBroker parentBroker, autopilotProcessimpl component,
			RollControlimpl rollControl, MixThrottlesControlimpl mixThrottlesControl, PitchControlimpl pitchControl,
			FlightMissionExecutionControlimpl flightMissionExecutionControl, PositionControlimpl positionControl,
			AltitudeControlimpl altitudeControl, HeadingControlimpl headingControl, DataLoggerimpl dataLogger) {
		this.parentBroker = parentBroker;
		this.component = component;
		this.rollControl = rollControl;
		this.altitudeControl = altitudeControl;
		this.headingControl = headingControl;
		this.dataLogger = dataLogger;
		this.pitchControl = pitchControl;
		this.flightMissionExecutionControl = flightMissionExecutionControl;
		this.positionControl = positionControl;
		this.mixThrottlesControl = mixThrottlesControl;
	}

	@Override
	public void sendOnConnection(String connection, Object data) {
		switch (connection) {
		case "con_1":
			// "positionInformationIn" is the target feature of "con_1" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			flightMissionExecutionControl.inPositionInformationIn((InformationPositionInformation) data);
			break;
		case "con_2":
			// "shutdownCommandIn" is the target feature of "con_2" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			mixThrottlesControl.inShutdownCommandIn((CommandShutdownCommand) data);
			break;
		case "con_3":
			// "shutdownCommandIn" is the target feature of "con_3" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			positionControl.inShutdownCommandIn((CommandShutdownCommand) data);
			break;
		case "con_4":
			// "positionCommandIn" is the target feature of "con_4" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			positionControl.inPositionCommandIn((CommandPositionCommand) data);
			break;
		case "con_5":
			// "positionInformationIn" is the target feature of "con_5" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			positionControl.inPositionInformationIn((InformationPositionInformation) data);
			break;
		case "con_6":
			// "commandIn" is the target feature of "con_6" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			altitudeControl.inCommandIn((CommandAltitudeCommand) data);
			break;
		case "con_7":
			// "commandIn" is the target feature of "con_7" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			rollControl.inCommandIn((CommandRollCommand) data);
			break;
		case "con_8":
			// "positionIn" is the target feature of "con_8" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			altitudeControl.inPositionIn((InformationPositionInformation) data);
			break;
		case "con_9":
			// "positionIn" is the target feature of "con_9" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			rollControl.inPositionIn((InformationPositionInformation) data);
			break;
		case "con_10":
			// "positionIn" is the target feature of "con_10" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			pitchControl.inPositionIn((InformationPositionInformation) data);
			break;
		case "con_11":
			// "positionIn" is the target feature of "con_11" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			headingControl.inPositionIn((InformationPositionInformation) data);
			break;
		case "con_12":
			// "shutdownCommandIn" is the target feature of "con_12" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			altitudeControl.inShutdownCommandIn((CommandShutdownCommand) data);
			break;
		case "con_13":
			// "shutdownCommandIn" is the target feature of "con_13" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			rollControl.inShutdownCommandIn((CommandShutdownCommand) data);
			break;
		case "con_14":
			// "shutdownCommandIn" is the target feature of "con_14" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			pitchControl.inShutdownCommandIn((CommandShutdownCommand) data);
			break;
		case "con_15":
			// "shutdownCommandIn" is the target feature of "con_15" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			headingControl.inShutdownCommandIn((CommandShutdownCommand) data);
			break;
		case "con_16":
			// "commandIn" is the target feature of "con_16" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			pitchControl.inCommandIn((CommandPitchCommand) data);
			break;
		case "con_17":
			// "commandIn" is the target feature of "con_17" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			headingControl.inCommandIn((CommandHeadingCommand) data);
			break;
		case "con_18":
			// "altitudeThrottleRequestIn" is the target feature of "con_18" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			mixThrottlesControl.inAltitudeThrottleRequestIn((RequestThrottleRequest) data);
			break;
		case "con_19":
			// "rollThrottleRequestIn" is the target feature of "con_19" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			mixThrottlesControl.inRollThrottleRequestIn((RequestThrottleRequest) data);
			break;
		case "con_20":
			// "pitchThrottleRequestIn" is the target feature of "con_20" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			mixThrottlesControl.inPitchThrottleRequestIn((RequestThrottleRequest) data);
			break;
		case "con_21":
			// "headingThrottleRequestIn" is the target feature of "con_21" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			mixThrottlesControl.inHeadingThrottleRequestIn((RequestThrottleRequest) data);
			break;
		case "con_22":
			// "throttleOut" is the target feature of "con_22" 
			// which is a connection from a subcomponent of "autopilot" to "autopilot" itself, 
			// so call its corresponding out mehtod
			component.outThrottleOut((CommandThrottleCommand) data);
			break;
		case "specialCon1":
			// "positionIn" is the target feature of "specialCon1" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			dataLogger.inPositionIn((InformationPositionInformation) data);
			break;
		case "specialCon2":
			// "throttleIn" is the target feature of "specialCon2" 
			// which is a connection between subcomponents of "autopilot", 
			// so call its corresponding in method
			dataLogger.inThrottleIn((CommandThrottleCommand) data);
			break;
		default:
			log.severe("No connection to send on found for: " + connection);
			break;
		}
	}

	@Override
	public void sendOnPort(String port, Object data) {
		switch (port) {
		case "rollControl.throttleRequestOut":
			sendOnConnection("con_19", data);
			break;
		case "mixThrottlesControl.throttleCommandOut":
			sendOnConnection("specialCon2", data);
			sendOnConnection("con_22", data);
			break;
		case "pitchControl.throttleRequestOut":
			sendOnConnection("con_20", data);
			break;
		case "flightMissionExecutionControl.shutdownCommandOut":
			sendOnConnection("con_2", data);
			sendOnConnection("con_3", data);
			break;
		case "flightMissionExecutionControl.positionCommandOut":
			sendOnConnection("con_4", data);
			break;
		case "flightMissionExecutionControl.positionInformationOut":
			sendOnConnection("con_5", data);
			break;
		case "positionControl.headingCommandOut":
			sendOnConnection("con_17", data);
			break;
		case "positionControl.shutdownCommandOut":
			sendOnConnection("con_12", data);
			sendOnConnection("con_13", data);
			sendOnConnection("con_14", data);
			sendOnConnection("con_15", data);
			break;
		case "positionControl.altitudeCommandOut":
			sendOnConnection("con_6", data);
			break;
		case "positionControl.rollCommandOut":
			sendOnConnection("con_7", data);
			break;
		case "positionControl.positionInformationOut":
			sendOnConnection("con_8", data);
			sendOnConnection("con_9", data);
			sendOnConnection("con_10", data);
			sendOnConnection("con_11", data);
			break;
		case "positionControl.pitchCommandOut":
			sendOnConnection("con_16", data);
			break;
		case "altitudeControl.throttleRequestOut":
			sendOnConnection("con_18", data);
			break;
		case "headingControl.throttleRequestOut":
			sendOnConnection("con_21", data);
			break;
		default:
			log.severe("No port to send on found for: " + port);
			break;
		}
	}

	@Override
	public Object getSynchronisationObjectForConnection(String connection) {
		switch (connection) {
		case "con_19":
			// Look up the syncObject, as throttleRequestOut which is the source feature of con_19 
			// has no ingoing connections within rollControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_19 = syncObjects.get("con_19");
			if (syncOncon_19 == null) {
				syncOncon_19 = new Object();
				syncObjects.put("con_19", syncOncon_19);
			}
			return syncOncon_19;
		case "specialCon2":
			// Look up the syncObject, as throttleCommandOut which is the source feature of specialCon2 
			// has no ingoing connections within mixThrottlesControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOnspecialCon2 = syncObjects.get("specialCon2");
			if (syncOnspecialCon2 == null) {
				syncOnspecialCon2 = new Object();
				syncObjects.put("specialCon2", syncOnspecialCon2);
			}
			return syncOnspecialCon2;
		case "con_20":
			// Look up the syncObject, as throttleRequestOut which is the source feature of con_20 
			// has no ingoing connections within pitchControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_20 = syncObjects.get("con_20");
			if (syncOncon_20 == null) {
				syncOncon_20 = new Object();
				syncObjects.put("con_20", syncOncon_20);
			}
			return syncOncon_20;
		case "con_2":
			// Look up the syncObject, as shutdownCommandOut which is the source feature of con_2 
			// has no ingoing connections within flightMissionExecutionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_2 = syncObjects.get("con_2");
			if (syncOncon_2 == null) {
				syncOncon_2 = new Object();
				syncObjects.put("con_2", syncOncon_2);
			}
			return syncOncon_2;
		case "con_3":
			// Look up the syncObject, as shutdownCommandOut which is the source feature of con_3 
			// has no ingoing connections within flightMissionExecutionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_3 = syncObjects.get("con_3");
			if (syncOncon_3 == null) {
				syncOncon_3 = new Object();
				syncObjects.put("con_3", syncOncon_3);
			}
			return syncOncon_3;
		case "con_4":
			// Look up the syncObject, as positionCommandOut which is the source feature of con_4 
			// has no ingoing connections within flightMissionExecutionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_4 = syncObjects.get("con_4");
			if (syncOncon_4 == null) {
				syncOncon_4 = new Object();
				syncObjects.put("con_4", syncOncon_4);
			}
			return syncOncon_4;
		case "con_5":
			// Look up the syncObject, as positionInformationOut which is the source feature of con_5 
			// has no ingoing connections within flightMissionExecutionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_5 = syncObjects.get("con_5");
			if (syncOncon_5 == null) {
				syncOncon_5 = new Object();
				syncObjects.put("con_5", syncOncon_5);
			}
			return syncOncon_5;
		case "con_6":
			// Look up the syncObject, as altitudeCommandOut which is the source feature of con_6 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_6 = syncObjects.get("con_6");
			if (syncOncon_6 == null) {
				syncOncon_6 = new Object();
				syncObjects.put("con_6", syncOncon_6);
			}
			return syncOncon_6;
		case "con_16":
			// Look up the syncObject, as pitchCommandOut which is the source feature of con_16 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_16 = syncObjects.get("con_16");
			if (syncOncon_16 == null) {
				syncOncon_16 = new Object();
				syncObjects.put("con_16", syncOncon_16);
			}
			return syncOncon_16;
		case "con_7":
			// Look up the syncObject, as rollCommandOut which is the source feature of con_7 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_7 = syncObjects.get("con_7");
			if (syncOncon_7 == null) {
				syncOncon_7 = new Object();
				syncObjects.put("con_7", syncOncon_7);
			}
			return syncOncon_7;
		case "con_17":
			// Look up the syncObject, as headingCommandOut which is the source feature of con_17 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_17 = syncObjects.get("con_17");
			if (syncOncon_17 == null) {
				syncOncon_17 = new Object();
				syncObjects.put("con_17", syncOncon_17);
			}
			return syncOncon_17;
		case "con_8":
			// Look up the syncObject, as positionInformationOut which is the source feature of con_8 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_8 = syncObjects.get("con_8");
			if (syncOncon_8 == null) {
				syncOncon_8 = new Object();
				syncObjects.put("con_8", syncOncon_8);
			}
			return syncOncon_8;
		case "con_9":
			// Look up the syncObject, as positionInformationOut which is the source feature of con_9 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_9 = syncObjects.get("con_9");
			if (syncOncon_9 == null) {
				syncOncon_9 = new Object();
				syncObjects.put("con_9", syncOncon_9);
			}
			return syncOncon_9;
		case "con_10":
			// Look up the syncObject, as positionInformationOut which is the source feature of con_10 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_10 = syncObjects.get("con_10");
			if (syncOncon_10 == null) {
				syncOncon_10 = new Object();
				syncObjects.put("con_10", syncOncon_10);
			}
			return syncOncon_10;
		case "con_11":
			// Look up the syncObject, as positionInformationOut which is the source feature of con_11 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_11 = syncObjects.get("con_11");
			if (syncOncon_11 == null) {
				syncOncon_11 = new Object();
				syncObjects.put("con_11", syncOncon_11);
			}
			return syncOncon_11;
		case "con_12":
			// Look up the syncObject, as shutdownCommandOut which is the source feature of con_12 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_12 = syncObjects.get("con_12");
			if (syncOncon_12 == null) {
				syncOncon_12 = new Object();
				syncObjects.put("con_12", syncOncon_12);
			}
			return syncOncon_12;
		case "con_13":
			// Look up the syncObject, as shutdownCommandOut which is the source feature of con_13 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_13 = syncObjects.get("con_13");
			if (syncOncon_13 == null) {
				syncOncon_13 = new Object();
				syncObjects.put("con_13", syncOncon_13);
			}
			return syncOncon_13;
		case "con_14":
			// Look up the syncObject, as shutdownCommandOut which is the source feature of con_14 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_14 = syncObjects.get("con_14");
			if (syncOncon_14 == null) {
				syncOncon_14 = new Object();
				syncObjects.put("con_14", syncOncon_14);
			}
			return syncOncon_14;
		case "con_15":
			// Look up the syncObject, as shutdownCommandOut which is the source feature of con_15 
			// has no ingoing connections within positionControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_15 = syncObjects.get("con_15");
			if (syncOncon_15 == null) {
				syncOncon_15 = new Object();
				syncObjects.put("con_15", syncOncon_15);
			}
			return syncOncon_15;
		case "con_18":
			// Look up the syncObject, as throttleRequestOut which is the source feature of con_18 
			// has no ingoing connections within altitudeControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_18 = syncObjects.get("con_18");
			if (syncOncon_18 == null) {
				syncOncon_18 = new Object();
				syncObjects.put("con_18", syncOncon_18);
			}
			return syncOncon_18;
		case "con_21":
			// Look up the syncObject, as throttleRequestOut which is the source feature of con_21 
			// has no ingoing connections within headingControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_21 = syncObjects.get("con_21");
			if (syncOncon_21 == null) {
				syncOncon_21 = new Object();
				syncObjects.put("con_21", syncOncon_21);
			}
			return syncOncon_21;
		case "con_1":
			// Ask the parentBroker for the syncObject, as positionIn which is the source feature of con_1 
			// is a port of this component and not of a subcomponent. Thus, we must ask the parent component for a syncObject.
			return parentBroker.getSynchronisationObjectForPort("autopilot.positionIn");
		case "specialCon1":
			// Ask the parentBroker for the syncObject, as positionIn which is the source feature of specialCon1 
			// is a port of this component and not of a subcomponent. Thus, we must ask the parent component for a syncObject.
			return parentBroker.getSynchronisationObjectForPort("autopilot.positionIn");
		case "con_22":
			// Look up the syncObject, as throttleCommandOut which is the source feature of con_22 
			// has no ingoing connections within mixThrottlesControl (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOncon_22 = syncObjects.get("con_22");
			if (syncOncon_22 == null) {
				syncOncon_22 = new Object();
				syncObjects.put("con_22", syncOncon_22);
			}
			return syncOncon_22;
		default:
			log.severe("No sync object found for connectionname: " + connection);
			return null;
		}
	}

	@Override
	public Object getSynchronisationObjectForPort(String port) {
		switch (port) {
		case "flightMissionExecutionControl.positionInformationIn":
			// positionInformationIn is the target of con_1. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_1 for a syncObject.
			return getSynchronisationObjectForConnection("con_1");
		case "mixThrottlesControl.shutdownCommandIn":
			// shutdownCommandIn is the target of con_2. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_2 for a syncObject.
			return getSynchronisationObjectForConnection("con_2");
		case "positionControl.shutdownCommandIn":
			// shutdownCommandIn is the target of con_3. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_3 for a syncObject.
			return getSynchronisationObjectForConnection("con_3");
		case "positionControl.positionCommandIn":
			// positionCommandIn is the target of con_4. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_4 for a syncObject.
			return getSynchronisationObjectForConnection("con_4");
		case "positionControl.positionInformationIn":
			// positionInformationIn is the target of con_5. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_5 for a syncObject.
			return getSynchronisationObjectForConnection("con_5");
		case "altitudeControl.commandIn":
			// commandIn is the target of con_6. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_6 for a syncObject.
			return getSynchronisationObjectForConnection("con_6");
		case "rollControl.commandIn":
			// commandIn is the target of con_7. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_7 for a syncObject.
			return getSynchronisationObjectForConnection("con_7");
		case "altitudeControl.positionIn":
			// positionIn is the target of con_8. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_8 for a syncObject.
			return getSynchronisationObjectForConnection("con_8");
		case "rollControl.positionIn":
			// positionIn is the target of con_9. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_9 for a syncObject.
			return getSynchronisationObjectForConnection("con_9");
		case "pitchControl.positionIn":
			// positionIn is the target of con_10. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_10 for a syncObject.
			return getSynchronisationObjectForConnection("con_10");
		case "headingControl.positionIn":
			// positionIn is the target of con_11. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_11 for a syncObject.
			return getSynchronisationObjectForConnection("con_11");
		case "altitudeControl.shutdownCommandIn":
			// shutdownCommandIn is the target of con_12. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_12 for a syncObject.
			return getSynchronisationObjectForConnection("con_12");
		case "rollControl.shutdownCommandIn":
			// shutdownCommandIn is the target of con_13. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_13 for a syncObject.
			return getSynchronisationObjectForConnection("con_13");
		case "pitchControl.shutdownCommandIn":
			// shutdownCommandIn is the target of con_14. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_14 for a syncObject.
			return getSynchronisationObjectForConnection("con_14");
		case "headingControl.shutdownCommandIn":
			// shutdownCommandIn is the target of con_15. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_15 for a syncObject.
			return getSynchronisationObjectForConnection("con_15");
		case "pitchControl.commandIn":
			// commandIn is the target of con_16. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_16 for a syncObject.
			return getSynchronisationObjectForConnection("con_16");
		case "headingControl.commandIn":
			// commandIn is the target of con_17. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_17 for a syncObject.
			return getSynchronisationObjectForConnection("con_17");
		case "mixThrottlesControl.altitudeThrottleRequestIn":
			// altitudeThrottleRequestIn is the target of con_18. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_18 for a syncObject.
			return getSynchronisationObjectForConnection("con_18");
		case "mixThrottlesControl.rollThrottleRequestIn":
			// rollThrottleRequestIn is the target of con_19. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_19 for a syncObject.
			return getSynchronisationObjectForConnection("con_19");
		case "mixThrottlesControl.pitchThrottleRequestIn":
			// pitchThrottleRequestIn is the target of con_20. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_20 for a syncObject.
			return getSynchronisationObjectForConnection("con_20");
		case "mixThrottlesControl.headingThrottleRequestIn":
			// headingThrottleRequestIn is the target of con_21. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on con_21 for a syncObject.
			return getSynchronisationObjectForConnection("con_21");
		case "dataLogger.positionIn":
			// positionIn is the target of specialCon1. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on specialCon1 for a syncObject.
			return getSynchronisationObjectForConnection("specialCon1");
		case "dataLogger.throttleIn":
			// throttleIn is the target of specialCon2. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on specialCon2 for a syncObject.
			return getSynchronisationObjectForConnection("specialCon2");
		default:
			log.severe("No sync object found for portname: " + port);
			return null;
		}
	}
}
