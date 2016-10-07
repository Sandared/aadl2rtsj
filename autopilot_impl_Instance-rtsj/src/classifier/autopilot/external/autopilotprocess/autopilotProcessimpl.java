package classifier.autopilot.external.autopilotprocess;

//########## Classifier Imports ##########
import classifier.autopilot.external.altitudecontrol.AltitudeControlimpl;
import classifier.autopilot.external.autopilotprocess.AutopilotProcess;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.datalogger.DataLoggerimpl;
import classifier.autopilot.external.flightmissionexecutioncontrol.FlightMissionExecutionControlimpl;
import classifier.autopilot.external.headingcontrol.HeadingControlimpl;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.mixthrottlescontrol.MixThrottlesControlimpl;
import classifier.autopilot.external.pitchcontrol.PitchControlimpl;
import classifier.autopilot.external.positioncontrol.PositionControlimpl;
import classifier.autopilot.external.rollcontrol.RollControlimpl;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

/**
 * This class represents the model element autopilotProcess.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within autopilotProcess.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class autopilotProcessimpl implements AutopilotProcess {

	/**
	 * ConnectionBroker to send on outgoing connections. Has to be set by the parent component.
	 */
	protected ConnectionBroker parentBroker;

	/**
	 * ConnectionBroker for the connections within this Thread
	 */
	protected ConnectionBroker broker;

	//########## Subcomponent Variables ##########
	/**
	 * A member variable that is used to store the subcomponent <code>rollControl</code>
	 */
	protected RollControlimpl rollControl;

	/**
	 * A member variable that is used to store the subcomponent <code>mixThrottlesControl</code>
	 */
	protected MixThrottlesControlimpl mixThrottlesControl;

	/**
	 * A member variable that is used to store the subcomponent <code>pitchControl</code>
	 */
	protected PitchControlimpl pitchControl;

	/**
	 * A member variable that is used to store the subcomponent <code>flightMissionExecutionControl</code>
	 */
	protected FlightMissionExecutionControlimpl flightMissionExecutionControl;

	/**
	 * A member variable that is used to store the subcomponent <code>positionControl</code>
	 */
	protected PositionControlimpl positionControl;

	/**
	 * A member variable that is used to store the subcomponent <code>altitudeControl</code>
	 */
	protected AltitudeControlimpl altitudeControl;

	/**
	 * A member variable that is used to store the subcomponent <code>headingControl</code>
	 */
	protected HeadingControlimpl headingControl;

	/**
	 * A member variable that is used to store the subcomponent <code>dataLogger</code>
	 */
	protected DataLoggerimpl dataLogger;

	//########## Data Port Variables ###########
	/**
	 * A DataPort<CommandThrottleCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>throttleOut</code>
	 */
	protected DataPort<CommandThrottleCommand> throttleOut = new DataPort<CommandThrottleCommand>();

	//########## Inherited Methods from AutopilotProcess ###########
	/**
	 * This method forwards <code>data</code> on its respective internal connections</br>
	 * @param data the data that is sent to this thread via its <code>positionIn</code> IN DATA PORT 
	 */
	@Override
	public void inPositionIn(InformationPositionInformation data) {
		broker.sendOnConnection("con_1", data);
		broker.sendOnConnection("specialCon1", data);
	}

}
