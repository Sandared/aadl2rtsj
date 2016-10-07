package user.autopilot_impl.autopilot.mixthrottlescontrol;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InOutPort;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.request.RequestThrottleRequest;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code addtionalImports
import common.MixData;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;
// End of user code

public class mixThrottlesControlUserCode {

	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(mixThrottlesControlUserCode.class.getName());

	private OutPort<CommandThrottleCommand> throttleCommandOut;
	private InPort<RequestThrottleRequest> rollThrottleRequestIn;
	private InPort<RequestThrottleRequest> pitchThrottleRequestIn;
	private InPort<RequestThrottleRequest> headingThrottleRequestIn;
	private InPort<RequestThrottleRequest> altitudeThrottleRequestIn;
	private InPort<CommandShutdownCommand> shutdownCommandIn;

	// Start of user code additionalVariables
	private MixData mixData = new MixData();
	// End of user code

	public mixThrottlesControlUserCode(OutPort<CommandThrottleCommand> throttleCommandOut,
			InPort<RequestThrottleRequest> rollThrottleRequestIn, InPort<RequestThrottleRequest> pitchThrottleRequestIn,
			InPort<RequestThrottleRequest> headingThrottleRequestIn,
			InPort<RequestThrottleRequest> altitudeThrottleRequestIn,
			InPort<CommandShutdownCommand> shutdownCommandIn) {
		this.throttleCommandOut = throttleCommandOut;
		this.rollThrottleRequestIn = rollThrottleRequestIn;
		this.pitchThrottleRequestIn = pitchThrottleRequestIn;
		this.headingThrottleRequestIn = headingThrottleRequestIn;
		this.altitudeThrottleRequestIn = altitudeThrottleRequestIn;
		this.shutdownCommandIn = shutdownCommandIn;
		// Start of user code initialization
		// TODO: place your own initialization code here
		// End of user code
	}

	public void dispatch() {
		// Start of user code dispatch
		// End of user code
	}

	public void start() {
		// Start of user code start
		// End of user code
	}

	public void completion() {
		// Start of user code completion
		// End of user code
	}

	public void compute() {
		// Start of user code compute
		if (altitudeThrottleRequestIn.isDirty())
			try {
				mixData.setAltitudeControlMessage(altitudeThrottleRequestIn.getData());
			} catch (NoDataException e) {
				log.info("No data on in port");
			}
		if (headingThrottleRequestIn.isDirty())
			try {
				mixData.setOrientationControlMessage(headingThrottleRequestIn.getData());
			} catch (NoDataException e) {
				log.info("No data on in port");
			}
		if (pitchThrottleRequestIn.isDirty())
			try {
				mixData.setPitchControlMessage(pitchThrottleRequestIn.getData());
			} catch (NoDataException e) {
				log.info("No data on in port");
			}
		if (rollThrottleRequestIn.isDirty())
			try {
				mixData.setRollControlMessage(rollThrottleRequestIn.getData());
			} catch (NoDataException e) {
				log.info("No data on in port");
			}
		if (mixData.hasAllData()) {
			final float mixedThrottleOneValue = mixThrottleRequests(
					mixData.getAltitudeControlMessage().getThrottleRequestEngine1().getValue(),
					mixData.getPitchControlMessage().getThrottleRequestEngine1().getValue(),
					mixData.getRollControlMessage().getThrottleRequestEngine1().getValue(),
					mixData.getOrientationControlMessage().getThrottleRequestEngine1().getValue());

			final float mixedThrottleTwoValue = mixThrottleRequests(
					mixData.getAltitudeControlMessage().getThrottleRequestEngine2().getValue(),
					mixData.getPitchControlMessage().getThrottleRequestEngine2().getValue(),
					mixData.getRollControlMessage().getThrottleRequestEngine2().getValue(),
					mixData.getOrientationControlMessage().getThrottleRequestEngine2().getValue());

			final float mixedThrottleThreeValue = mixThrottleRequests(
					mixData.getAltitudeControlMessage().getThrottleRequestEngine3().getValue(),
					mixData.getPitchControlMessage().getThrottleRequestEngine3().getValue(),
					mixData.getRollControlMessage().getThrottleRequestEngine3().getValue(),
					mixData.getOrientationControlMessage().getThrottleRequestEngine3().getValue());

			final float mixedThrottleFourValue = mixThrottleRequests(
					mixData.getAltitudeControlMessage().getThrottleRequestEngine4().getValue(),
					mixData.getPitchControlMessage().getThrottleRequestEngine4().getValue(),
					mixData.getRollControlMessage().getThrottleRequestEngine4().getValue(),
					mixData.getOrientationControlMessage().getThrottleRequestEngine4().getValue());

			// Create ThrottleCommands, and send them to XPlane
			CommandThrottleCommand throttleCommand = new CommandThrottleCommand();
			throttleCommand.getThrottleEngine1().setValue(mixedThrottleOneValue);
			throttleCommand.getThrottleEngine2().setValue(mixedThrottleTwoValue);
			throttleCommand.getThrottleEngine3().setValue(mixedThrottleThreeValue);
			throttleCommand.getThrottleEngine4().setValue(mixedThrottleFourValue);

			throttleCommandOut.setData(throttleCommand);

			mixData = new MixData();
		}
		// End of user code
	}

	// Start of user code helperstuff
	// Limit output to [0;1]
	public float mixThrottleRequests(float altitude, float pitch, float roll, float orientation) {
		float setting = altitude + pitch + roll + orientation;
		if (setting < 0) {
			return 0;
		} else if (setting > 1) {
			return 1;
		} else {
			return setting;
		}
	}
	// End of user code
}
