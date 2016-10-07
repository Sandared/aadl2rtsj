package classifier.autopilot.external.positioncontrol;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandAltitudeCommand;
import classifier.autopilot.external.command.CommandHeadingCommand;
import classifier.autopilot.external.command.CommandPitchCommand;
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandRollCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.positioncontrol.PositionControl;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

//########## ConnectionBrokerable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This class represents the model element PositionControl.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within PositionControl.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class PositionControlimpl implements PositionControl, ConnectionBrokerable {

	//########## Subcomponent Variables ##########

	//########## Data Port Variables ###########
	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionInformationIn</code>
	 */
	protected DataPort<InformationPositionInformation> positionInformationIn = new DataPort<InformationPositionInformation>();

	/**
	 * A DataPort<CommandPositionCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionCommandIn</code>
	 */
	protected DataPort<CommandPositionCommand> positionCommandIn = new DataPort<CommandPositionCommand>();

	/**
	 * A DataPort<CommandShutdownCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>shutdownCommandIn</code>
	 */
	protected DataPort<CommandShutdownCommand> shutdownCommandIn = new DataPort<CommandShutdownCommand>();

	/**
	 * A DataPort<CommandAltitudeCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>altitudeCommandOut</code>
	 */
	protected DataPort<CommandAltitudeCommand> altitudeCommandOut = new DataPort<CommandAltitudeCommand>();

	/**
	 * A DataPort<CommandPitchCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>pitchCommandOut</code>
	 */
	protected DataPort<CommandPitchCommand> pitchCommandOut = new DataPort<CommandPitchCommand>();

	/**
	 * A DataPort<CommandRollCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>rollCommandOut</code>
	 */
	protected DataPort<CommandRollCommand> rollCommandOut = new DataPort<CommandRollCommand>();

	/**
	 * A DataPort<CommandHeadingCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>headingCommandOut</code>
	 */
	protected DataPort<CommandHeadingCommand> headingCommandOut = new DataPort<CommandHeadingCommand>();

	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionInformationOut</code>
	 */
	protected DataPort<InformationPositionInformation> positionInformationOut = new DataPort<InformationPositionInformation>();

	/**
	 * A DataPort<CommandShutdownCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>shutdownCommandOut</code>
	 */
	protected DataPort<CommandShutdownCommand> shutdownCommandOut = new DataPort<CommandShutdownCommand>();

	//########## Inherited Methods from PositionControl ###########
	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>positionInformationIn</code> IN DATA PORT 
	 */
	@Override
	public void inPositionInformationIn(InformationPositionInformation data) {
		// store the data in its corresponding positionInformationIn variable
		this.positionInformationIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>positionCommandIn</code> IN DATA PORT 
	 */
	@Override
	public void inPositionCommandIn(CommandPositionCommand data) {
		// store the data in its corresponding positionCommandIn variable
		this.positionCommandIn.setFWData(data);
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

}
