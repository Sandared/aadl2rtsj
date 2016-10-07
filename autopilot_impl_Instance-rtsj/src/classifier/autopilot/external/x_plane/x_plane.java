package classifier.autopilot.external.x_plane;

//########## Classifier Import##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This interface represents the model element x_plane</br>
 * Each DATA PORT of x_plane is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface x_plane extends ConnectionBrokerable {
	/**
	 * This method broadcasts the given data on all outgoing connections of <code>position</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>position</code>
	 */
	void outPosition(InformationPositionInformation data);

	void inThrottle(CommandThrottleCommand data);
}
