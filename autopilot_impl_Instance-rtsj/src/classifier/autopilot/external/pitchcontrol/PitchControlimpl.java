package classifier.autopilot.external.pitchcontrol;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.autopilot.external.command.CommandPitchCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.pitchcontrol.PitchControl;
import classifier.autopilot.external.request.RequestThrottleRequest;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

//########## ConnectionBrokerable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This class represents the model element PitchControl.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within PitchControl.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class PitchControlimpl implements PitchControl, ConnectionBrokerable {

	//########## Subcomponent Variables ##########

	//########## Data Port Variables ###########
	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionIn</code>
	 */
	protected DataPort<InformationPositionInformation> positionIn = new DataPort<InformationPositionInformation>();

	/**
	 * A DataPort<RequestThrottleRequest> member variable that is</br> 
	 * used to store data for the DATA PORT <code>throttleRequestOut</code>
	 */
	protected DataPort<RequestThrottleRequest> throttleRequestOut = new DataPort<RequestThrottleRequest>();

	/**
	 * A DataPort<CommandShutdownCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>shutdownCommandIn</code>
	 */
	protected DataPort<CommandShutdownCommand> shutdownCommandIn = new DataPort<CommandShutdownCommand>();

	/**
	 * A DataPort<CommandPitchCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>commandIn</code>
	 */
	protected DataPort<CommandPitchCommand> commandIn = new DataPort<CommandPitchCommand>();

	//########## Inherited Methods from PitchControl ###########
	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>positionIn</code> IN DATA PORT 
	 */
	@Override
	public void inPositionIn(InformationPositionInformation data) {
		// store the data in its corresponding positionIn variable
		this.positionIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>shutdownCommandIn</code> IN DATA PORT 
	 */
	@Override
	public void inShutdownCommandIn(CommandShutdownCommand data) {
		// store the data in its corresponding shutdownCommandIn variable
		this.shutdownCommandIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>commandIn</code> IN DATA PORT 
	 */
	@Override
	public void inCommandIn(CommandPitchCommand data) {
		// store the data in its corresponding commandIn variable
		this.commandIn.setFWData(data);
	}

}
