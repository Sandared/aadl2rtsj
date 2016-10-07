package classifier.autopilot.external.flightmissionexecutioncontrol;

//########## Classifier Import ##########
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

/**
 * This interface represents the model element FlightMissionExecutionControl</br>
 * Each DATA PORT of FlightMissionExecutionControl is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface FlightMissionExecutionControl {
	void inPositionInformationIn(InformationPositionInformation data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>shutdownCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>shutdownCommandOut</code>
	 */
	void outShutdownCommandOut(CommandShutdownCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>positionCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>positionCommandOut</code>
	 */
	void outPositionCommandOut(CommandPositionCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>positionInformationOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>positionInformationOut</code>
	 */
	void outPositionInformationOut(InformationPositionInformation data);
}
