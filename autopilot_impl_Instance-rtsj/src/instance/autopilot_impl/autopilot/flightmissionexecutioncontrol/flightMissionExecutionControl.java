package instance.autopilot_impl.autopilot.flightmissionexecutioncontrol;

import javax.realtime.Timer;
import javax.realtime.OneShotTimer;
import javax.realtime.RelativeTime;
import javax.realtime.PeriodicTimer;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PriorityParameters;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandPositionCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.flightmissionexecutioncontrol.FlightMissionExecutionControlimpl;
import classifier.autopilot.external.information.InformationPositionInformation;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

//########## Usercode Import ##########
import user.autopilot_impl.autopilot.flightmissionexecutioncontrol.flightMissionExecutionControlUserCode;

/**
 * This class represents the instance model element flightMissionExecutionControl.</br>
 * It defines a ConnectionBroker for the handling of connections defined within flightMissionExecutionControl and a parent ConnectionBroker to forward or initiate data transfer into 'higher' nested components.</br>
 * It defines am out'dataPortName' method for each OUT DATA PORT, which broadcasts the given data over the parentBroker on all outgoing connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class flightMissionExecutionControl extends FlightMissionExecutionControlimpl {
	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(flightMissionExecutionControl.class.getName());

	/**
	 * The AsyncEventHandler which makes this class an executable entity
	 */
	private InnerAsyncEventHandler handler = new InnerAsyncEventHandler();

	/**
	 * The timer, that triggers the AsyncEventHandler in a periodic manner
	 */
	private Timer timer = new PeriodicTimer(new RelativeTime(), new RelativeTime(50, 0), handler);

	/**
	 * ConnectionBroker to send on outgoing connections. Has to be set by the parent component.
	 */
	private ConnectionBroker parentBroker;

	/**
	 * ConnectionBroker for the connections within this Thread
	 */
	private ConnectionBroker broker;

	/**
	 * Usercode class for this Thread
	 */
	private flightMissionExecutionControlUserCode userCode = new flightMissionExecutionControlUserCode(
			positionInformationIn.getUserInPort(), shutdownCommandOut.getUserOutPort(),
			positionCommandOut.getUserOutPort(), positionInformationOut.getUserOutPort());

	public flightMissionExecutionControl() {
	}

	//########## Inherited Methods from ConnectionBrokerable ##########
	@Override
	public void setParentConnectionBroker(ConnectionBroker parentBroker) {
		this.parentBroker = parentBroker;
		broker = new flightMissionExecutionControlConnectionBroker(parentBroker, this);
	}

	@Override
	public ConnectionBroker getConnectionBroker() {
		return broker;
	}

	//########## Inherited Methods from  FlightMissionExecutionControlimpl##########
	@Override
	public void outShutdownCommandOut(CommandShutdownCommand data) {
		parentBroker.sendOnPort("flightMissionExecutionControl.shutdownCommandOut", data);
	}

	@Override
	public void outPositionCommandOut(CommandPositionCommand data) {
		parentBroker.sendOnPort("flightMissionExecutionControl.positionCommandOut", data);
	}

	@Override
	public void outPositionInformationOut(InformationPositionInformation data) {
		parentBroker.sendOnPort("flightMissionExecutionControl.positionInformationOut", data);
	}

	/**
	 * Method that is called by the AsyncEventHandler in order to map the dispatch event of an AADL thread.
	 */
	private final void dispatch() {

		positionInformationIn.receiveInput();
		//Execute User Code
		userCode.dispatch();
	}

	/**
	 * Method that is called by the AsyncEventHandler in order to map the start event of an AADL thread.
	 */
	private final void start() {

		//Execute User Code
		userCode.start();
	}

	/**
	 * Method that is called by the AsyncEventHandler in order to map the compute time of an AADL thread.
	 */
	private final void compute() {
		//Execute User Code
		userCode.compute();
	}

	/**
	 * Method that is called by the AsyncEventHandler in order to map the completion event of an AADL thread.
	 */
	private final void completion() {
		//Execute User Code
		userCode.completion();
		parentBroker.sendOnConnection("con_3", shutdownCommandOut.getFWData());
		parentBroker.sendOnConnection("con_2", shutdownCommandOut.getFWData());
		parentBroker.sendOnConnection("con_4", positionCommandOut.getFWData());

		parentBroker.sendOnConnection("con_5", positionInformationOut.getFWData());
	}

	/**
	 * Starts the execution of this object
	 */
	public void startExecution() {
		timer.start();
	}

	/**
	 * Stops the execution of this object
	 */
	public void stopExcution() {
		timer.stop();
	}

	class InnerAsyncEventHandler extends AsyncEventHandler {
		public InnerAsyncEventHandler() {
			setDaemon(false);
			setSchedulingParameters(new PriorityParameters(5));
		}

		@Override
		public void handleAsyncEvent() {
			dispatch();
			start();
			compute();
			completion();
		}
	}
}
