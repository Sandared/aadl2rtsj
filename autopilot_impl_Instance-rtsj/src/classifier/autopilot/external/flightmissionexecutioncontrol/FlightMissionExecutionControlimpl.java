package classifier.autopilot.external.flightmissionexecutioncontrol;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.flightmissionexecutioncontrol.FlightMissionExecutionControl;
import classifier.autopilot.external.information.InformationPositionInformation;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

//########## ConnectionBrokerable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This class represents the model element FlightMissionExecutionControl.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within FlightMissionExecutionControl.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class FlightMissionExecutionControlimpl implements FlightMissionExecutionControl, ConnectionBrokerable {

	//########## Subcomponent Variables ##########

	//########## Data Port Variables ###########
	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionInformationIn</code>
	 */
	protected DataPort<InformationPositionInformation> positionInformationIn = new DataPort<InformationPositionInformation>();

	/**
	 * A DataPort<CommandShutdownCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>shutdownCommandOut</code>
	 */
	protected DataPort<CommandShutdownCommand> shutdownCommandOut = new DataPort<CommandShutdownCommand>();

	/**
	 * A DataPort<CommandPositionCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionCommandOut</code>
	 */
	protected DataPort<CommandPositionCommand> positionCommandOut = new DataPort<CommandPositionCommand>();

	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionInformationOut</code>
	 */
	protected DataPort<InformationPositionInformation> positionInformationOut = new DataPort<InformationPositionInformation>();

	//########## Inherited Methods from FlightMissionExecutionControl ###########
	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>positionInformationIn</code> IN DATA PORT 
	 */
	@Override
	public void inPositionInformationIn(InformationPositionInformation data) {
		// store the data in its corresponding positionInformationIn variable
		this.positionInformationIn.setFWData(data);
	}

}
