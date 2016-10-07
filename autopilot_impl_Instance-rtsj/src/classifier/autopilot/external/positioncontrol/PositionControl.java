package classifier.autopilot.external.positioncontrol;

//########## Classifier Import ##########
import classifier.autopilot.external.command.CommandAltitudeCommand;
import classifier.autopilot.external.command.CommandHeadingCommand;
import classifier.autopilot.external.command.CommandPitchCommand;
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandRollCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

/**
 * This interface represents the model element PositionControl</br>
 * Each DATA PORT of PositionControl is represented by a correspong in'dataPortName' or out'dataPortName' method.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface PositionControl {
	void inPositionInformationIn(InformationPositionInformation data);

	void inPositionCommandIn(CommandPositionCommand data);

	void inShutdownCommandIn(CommandShutdownCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>altitudeCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>altitudeCommandOut</code>
	 */
	void outAltitudeCommandOut(CommandAltitudeCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>pitchCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>pitchCommandOut</code>
	 */
	void outPitchCommandOut(CommandPitchCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>rollCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>rollCommandOut</code>
	 */
	void outRollCommandOut(CommandRollCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>headingCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>headingCommandOut</code>
	 */
	void outHeadingCommandOut(CommandHeadingCommand data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>positionInformationOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>positionInformationOut</code>
	 */
	void outPositionInformationOut(InformationPositionInformation data);

	/**
	 * This method broadcasts the given data on all outgoing connections of <code>shutdownCommandOut</code></br>
	 * via the <code>parentBroker</code> of this thread.
	 * @param data the data to be broadcasted on the OUT DATA PORT <code>shutdownCommandOut</code>
	 */
	void outShutdownCommandOut(CommandShutdownCommand data);
}
