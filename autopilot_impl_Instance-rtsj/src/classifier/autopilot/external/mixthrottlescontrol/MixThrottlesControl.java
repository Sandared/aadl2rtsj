package classifier.autopilot.external.mixthrottlescontrol;

//########## Classifier Import ##########
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.request.RequestThrottleRequest;

/**
 * This interface represents the model element MixThrottlesControl</br>
 * Each DATA PORT of MixThrottlesControl is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface MixThrottlesControl {
	/**
	 * This method broadcasts the given data on all outgoing connections of <code>throttleCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>throttleCommandOut</code>
	 */
	void outThrottleCommandOut(CommandThrottleCommand data);

	void inRollThrottleRequestIn(RequestThrottleRequest data);

	void inPitchThrottleRequestIn(RequestThrottleRequest data);

	void inHeadingThrottleRequestIn(RequestThrottleRequest data);

	void inAltitudeThrottleRequestIn(RequestThrottleRequest data);

	void inShutdownCommandIn(CommandShutdownCommand data);
}
