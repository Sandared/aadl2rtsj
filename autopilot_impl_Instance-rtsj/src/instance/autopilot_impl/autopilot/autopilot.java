package instance.autopilot_impl.autopilot;

//########## Classifier Imports ##########
import classifier.autopilot.external.altitudecontrol.AltitudeControlimpl;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.datalogger.DataLoggerimpl;
import classifier.autopilot.external.flightmissionexecutioncontrol.FlightMissionExecutionControlimpl;
import classifier.autopilot.external.headingcontrol.HeadingControlimpl;
import classifier.autopilot.external.mixthrottlescontrol.MixThrottlesControlimpl;
import classifier.autopilot.external.pitchcontrol.PitchControlimpl;
import classifier.autopilot.external.positioncontrol.PositionControlimpl;
import classifier.autopilot.external.rollcontrol.RollControlimpl;
import classifier.autopilot.external.autopilotprocess.autopilotProcessimpl;

//########## Logger Import ##########
import java.util.logging.Logger;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

/**
 * This class represents the instance model element autopilot.</br>
 * It defines a ConnectionBroker for the handling of connections defined within autopilot and a parent ConnectionBroker to forward or initiate data transfer into 'higher' nested components.</br>
 * It defines an out'dataPortName' method for each OUT DATA PORT, which brodcasts the given data over the parentBroker on all outgoing connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class autopilot extends autopilotProcessimpl {
	private static final Logger log = Logger.getLogger(autopilot.class.getName());

	public autopilot(RollControlimpl rollControl, MixThrottlesControlimpl mixThrottlesControl,
			PitchControlimpl pitchControl, FlightMissionExecutionControlimpl flightMissionExecutionControl,
			PositionControlimpl positionControl, AltitudeControlimpl altitudeControl, HeadingControlimpl headingControl,
			DataLoggerimpl dataLogger) {
		this.rollControl = rollControl;
		this.mixThrottlesControl = mixThrottlesControl;
		this.pitchControl = pitchControl;
		this.flightMissionExecutionControl = flightMissionExecutionControl;
		this.positionControl = positionControl;
		this.altitudeControl = altitudeControl;
		this.headingControl = headingControl;
		this.dataLogger = dataLogger;
	}

	//########## Inherited Methods from ConnectionBrokerable ##########
	@Override
	public void setParentConnectionBroker(ConnectionBroker parentBroker) {
		this.parentBroker = parentBroker;
		broker = new autopilotConnectionBroker(parentBroker, this, rollControl, mixThrottlesControl, pitchControl,
				flightMissionExecutionControl, positionControl, altitudeControl, headingControl, dataLogger);
		broker.addChildBroker("rollControl", rollControl.getConnectionBroker());
		rollControl.setParentConnectionBroker(broker);
		broker.addChildBroker("mixThrottlesControl", mixThrottlesControl.getConnectionBroker());
		mixThrottlesControl.setParentConnectionBroker(broker);
		broker.addChildBroker("pitchControl", pitchControl.getConnectionBroker());
		pitchControl.setParentConnectionBroker(broker);
		broker.addChildBroker("flightMissionExecutionControl", flightMissionExecutionControl.getConnectionBroker());
		flightMissionExecutionControl.setParentConnectionBroker(broker);
		broker.addChildBroker("positionControl", positionControl.getConnectionBroker());
		positionControl.setParentConnectionBroker(broker);
		broker.addChildBroker("altitudeControl", altitudeControl.getConnectionBroker());
		altitudeControl.setParentConnectionBroker(broker);
		broker.addChildBroker("headingControl", headingControl.getConnectionBroker());
		headingControl.setParentConnectionBroker(broker);
		broker.addChildBroker("dataLogger", dataLogger.getConnectionBroker());
		dataLogger.setParentConnectionBroker(broker);
	}

	@Override
	public ConnectionBroker getConnectionBroker() {
		return broker;
	}

	@Override
	public void outThrottleOut(CommandThrottleCommand data) {
		parentBroker.sendOnPort("autopilot.throttleOut", data);
	}

}
