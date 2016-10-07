package classifier.autopilot.external.headingcontrol;

//########## Classifier Import ##########
import classifier.autopilot.external.basiccontroller.BasicController;
import classifier.autopilot.external.command.Command;
import classifier.autopilot.external.command.CommandHeadingCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.request.RequestThrottleRequest;

/**
 * This interface represents the model element HeadingControl</br>
 * Each DATA PORT of HeadingControl is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface HeadingControl extends BasicController {
	void inPositionIn(InformationPositionInformation data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>throttleRequestOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>throttleRequestOut</code>
	 */
	void outThrottleRequestOut(RequestThrottleRequest data);

	void inShutdownCommandIn(CommandShutdownCommand data);

	/**
	 * This method is not meant for use, as it got refined by {@link #inCommandIn inCommandIn(CommandHeadingCommand data)}</br>
	 * If used nonetheless, this method throws a <tt>UnsupportedOperationException</tt>
	 * @exception UnsupportedOperationException if used
	 */
	@Override
	default void inCommandIn(Command data) {
		throw new UnsupportedOperationException();
	}

	void inCommandIn(CommandHeadingCommand data);
}
