package classifier.autopilot.external.x_plane;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.x_plane.x_plane;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

/**
 * This class represents the model element x_plane.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within x_plane.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class x_planeimpl implements x_plane {

	//########## Subcomponent Variables ##########

	//########## Data Port Variables ###########
	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>position</code>
	 */
	protected DataPort<InformationPositionInformation> position = new DataPort<InformationPositionInformation>();

	/**
	 * A DataPort<CommandThrottleCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>throttle</code>
	 */
	protected DataPort<CommandThrottleCommand> throttle = new DataPort<CommandThrottleCommand>();

	//########## Inherited Methods from x_plane ###########
	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>throttle</code> IN DATA PORT 
	 */
	@Override
	public void inThrottle(CommandThrottleCommand data) {
		this.throttle.setFWData(data);
	}

}
