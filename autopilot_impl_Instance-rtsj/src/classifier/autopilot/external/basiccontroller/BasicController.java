package classifier.autopilot.external.basiccontroller;

//########## Classifier Import ##########
import classifier.autopilot.external.command.Command;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.request.RequestThrottleRequest;

/**
 * This interface represents the model element BasicController</br>
 * Each DATA PORT of BasicController is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface BasicController {
	void inPositionIn(InformationPositionInformation data);

	void inCommandIn(Command data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>throttleRequestOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>throttleRequestOut</code>
	 */
	void outThrottleRequestOut(RequestThrottleRequest data);

	void inShutdownCommandIn(CommandShutdownCommand data);
}
