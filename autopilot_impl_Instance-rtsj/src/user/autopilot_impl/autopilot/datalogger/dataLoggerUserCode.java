package user.autopilot_impl.autopilot.datalogger;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InOutPort;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code addtionalImports
// TODO: place additionalImports here
// End of user code

public class dataLoggerUserCode {

	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(dataLoggerUserCode.class.getName());

	private InPort<CommandThrottleCommand> throttleIn;
	private InPort<InformationPositionInformation> positionIn;

	// Start of user code additionalVariables
	// TODO: place additional variables here
	// End of user code

	public dataLoggerUserCode(InPort<CommandThrottleCommand> throttleIn,
			InPort<InformationPositionInformation> positionIn) {
		this.throttleIn = throttleIn;
		this.positionIn = positionIn;
		// Start of user code initialization
		// TODO: place your own initialization code here
		// End of user code
	}

	public void dispatch() {
		// Start of user code dispatch
		// TODO: place dispatch code here
		// End of user code
	}

	public void start() {
		// Start of user code start
		// TODO: place start code here
		// End of user code
	}

	public void completion() {
		// Start of user code completion
		// TODO: place completion code here
		// End of user code
	}

	public void compute() {
		// Start of user code compute
		// TODO: place compute code here
		if(throttleIn.isDirty() && positionIn.isDirty()){
			try {
				CommandThrottleCommand throttle = throttleIn.getData();
				InformationPositionInformation position = positionIn.getData();
					
				log.info("Received: "
				+ "\n\t Latitude: " + position.getLocation().getLatitude().getValue()
				+ "\n\t Longitude: " + position.getLocation().getLongitude().getValue()
				+ "\n\t Altitude: " + position.getLocation().getAltitudeMsl().getValue());
				
				log.info("Sending: "
				+ "\n\t Throttle Engine 1: " + throttle.getThrottleEngine1().getValue()
				+ "\n\t Throttle Engine 1: " + throttle.getThrottleEngine1().getValue()
				+ "\n\t Throttle Engine 1: " + throttle.getThrottleEngine1().getValue()
				+ "\n\t Throttle Engine 1: " + throttle.getThrottleEngine1().getValue());
						
			} catch (NoDataException e) {
				//do nothing
			}
		}
		// End of user code
	}

	// Start of user code helperstuff
	// TODO: place helperstuff code here
	// End of user code
}
