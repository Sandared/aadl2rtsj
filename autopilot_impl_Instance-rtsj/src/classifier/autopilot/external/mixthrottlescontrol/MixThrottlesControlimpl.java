package classifier.autopilot.external.mixthrottlescontrol;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.mixthrottlescontrol.MixThrottlesControl;
import classifier.autopilot.external.request.RequestThrottleRequest;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

//########## ConnectionBrokerable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This class represents the model element MixThrottlesControl.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within MixThrottlesControl.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class MixThrottlesControlimpl implements MixThrottlesControl, ConnectionBrokerable {

	//########## Subcomponent Variables ##########

	//########## Data Port Variables ###########
	/**
	 * A DataPort<CommandThrottleCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>throttleCommandOut</code>
	 */
	protected DataPort<CommandThrottleCommand> throttleCommandOut = new DataPort<CommandThrottleCommand>();

	/**
	 * A DataPort<RequestThrottleRequest> member variable that is</br> 
	 * used to store data for the DATA PORT <code>rollThrottleRequestIn</code>
	 */
	protected DataPort<RequestThrottleRequest> rollThrottleRequestIn = new DataPort<RequestThrottleRequest>();

	/**
	 * A DataPort<RequestThrottleRequest> member variable that is</br> 
	 * used to store data for the DATA PORT <code>pitchThrottleRequestIn</code>
	 */
	protected DataPort<RequestThrottleRequest> pitchThrottleRequestIn = new DataPort<RequestThrottleRequest>();

	/**
	 * A DataPort<RequestThrottleRequest> member variable that is</br> 
	 * used to store data for the DATA PORT <code>headingThrottleRequestIn</code>
	 */
	protected DataPort<RequestThrottleRequest> headingThrottleRequestIn = new DataPort<RequestThrottleRequest>();

	/**
	 * A DataPort<RequestThrottleRequest> member variable that is</br> 
	 * used to store data for the DATA PORT <code>altitudeThrottleRequestIn</code>
	 */
	protected DataPort<RequestThrottleRequest> altitudeThrottleRequestIn = new DataPort<RequestThrottleRequest>();

	/**
	 * A DataPort<CommandShutdownCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>shutdownCommandIn</code>
	 */
	protected DataPort<CommandShutdownCommand> shutdownCommandIn = new DataPort<CommandShutdownCommand>();

	//########## Inherited Methods from MixThrottlesControl ###########
	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>rollThrottleRequestIn</code> IN DATA PORT 
	 */
	@Override
	public void inRollThrottleRequestIn(RequestThrottleRequest data) {
		// store the data in its corresponding rollThrottleRequestIn variable
		this.rollThrottleRequestIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>pitchThrottleRequestIn</code> IN DATA PORT 
	 */
	@Override
	public void inPitchThrottleRequestIn(RequestThrottleRequest data) {
		// store the data in its corresponding pitchThrottleRequestIn variable
		this.pitchThrottleRequestIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>headingThrottleRequestIn</code> IN DATA PORT 
	 */
	@Override
	public void inHeadingThrottleRequestIn(RequestThrottleRequest data) {
		// store the data in its corresponding headingThrottleRequestIn variable
		this.headingThrottleRequestIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>altitudeThrottleRequestIn</code> IN DATA PORT 
	 */
	@Override
	public void inAltitudeThrottleRequestIn(RequestThrottleRequest data) {
		// store the data in its corresponding altitudeThrottleRequestIn variable
		this.altitudeThrottleRequestIn.setFWData(data);
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
