package classifier.autopilot.external.datalogger;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.datalogger.DataLogger;
import classifier.autopilot.external.information.InformationPositionInformation;

//########## Port Variable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.DataPort;

//########## ConnectionBrokerable Import ##########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBrokerable;

/**
 * This class represents the model element DataLogger.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within DataLogger.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class DataLoggerimpl implements DataLogger, ConnectionBrokerable {

	//########## Subcomponent Variables ##########

	//########## Data Port Variables ###########
	/**
	 * A DataPort<CommandThrottleCommand> member variable that is</br> 
	 * used to store data for the DATA PORT <code>throttleIn</code>
	 */
	protected DataPort<CommandThrottleCommand> throttleIn = new DataPort<CommandThrottleCommand>();

	/**
	 * A DataPort<InformationPositionInformation> member variable that is</br> 
	 * used to store data for the DATA PORT <code>positionIn</code>
	 */
	protected DataPort<InformationPositionInformation> positionIn = new DataPort<InformationPositionInformation>();

	//########## Inherited Methods from DataLogger ###########
	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>throttleIn</code> IN DATA PORT 
	 */
	@Override
	public void inThrottleIn(CommandThrottleCommand data) {
		// store the data in its corresponding throttleIn variable
		this.throttleIn.setFWData(data);
	}

	/**
	 * This method stores <code>data</code> in an internal port variable.</br>
	 * @param data the data that is sent to this thread via its <code>positionIn</code> IN DATA PORT 
	 */
	@Override
	public void inPositionIn(InformationPositionInformation data) {
		// store the data in its corresponding positionIn variable
		this.positionIn.setFWData(data);
	}

}
