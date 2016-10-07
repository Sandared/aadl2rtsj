package classifier.autopilot.external.datalogger;

//########## Classifier Import ##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

/**
 * This interface represents the model element DataLogger</br>
 * Each DATA PORT of DataLogger is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface DataLogger {
	void inThrottleIn(CommandThrottleCommand data);

	void inPositionIn(InformationPositionInformation data);
}
