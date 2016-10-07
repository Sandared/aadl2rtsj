package classifier.autopilot.external.autopilotprocess;

//########## Classifier Import##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This interface represents the model element AutopilotProcess</br>
 * Each DATA PORT of AutopilotProcess is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface AutopilotProcess extends ConnectionBrokerable {
	void inPositionIn(InformationPositionInformation data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>throttleOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>throttleOut</code>
	 */
	void outThrottleOut(CommandThrottleCommand data);
}
