package instance.autopilot_impl.autopilot.mixthrottlescontrol;

import javax.realtime.Timer;
import javax.realtime.OneShotTimer;
import javax.realtime.RelativeTime;
import javax.realtime.PeriodicTimer;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PriorityParameters;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.mixthrottlescontrol.MixThrottlesControlimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

//########## Usercode Import ##########
import user.autopilot_impl.autopilot.mixthrottlescontrol.mixThrottlesControlUserCode;

/**
 * This class represents the instance model element mixThrottlesControl.</br>
 * It defines a ConnectionBroker for the handling of connections defined within mixThrottlesControl and a parent ConnectionBroker to forward or initiate data transfer into 'higher' nested components.</br>
 * It defines am out'dataPortName' method for each OUT DATA PORT, which broadcasts the given data over the parentBroker on all outgoing connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class mixThrottlesControl extends MixThrottlesControlimpl {
	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(mixThrottlesControl.class.getName());

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
	private mixThrottlesControlUserCode userCode = new mixThrottlesControlUserCode(throttleCommandOut.getUserOutPort(),
			rollThrottleRequestIn.getUserInPort(), pitchThrottleRequestIn.getUserInPort(),
			headingThrottleRequestIn.getUserInPort(), altitudeThrottleRequestIn.getUserInPort(),
			shutdownCommandIn.getUserInPort());

	public mixThrottlesControl() {
	}

	//########## Inherited Methods from ConnectionBrokerable ##########
	@Override
	public void setParentConnectionBroker(ConnectionBroker parentBroker) {
		this.parentBroker = parentBroker;
		broker = new mixThrottlesControlConnectionBroker(parentBroker, this);
	}

	@Override
	public ConnectionBroker getConnectionBroker() {
		return broker;
	}

	//########## Inherited Methods from  MixThrottlesControlimpl##########
	@Override
	public void outThrottleCommandOut(CommandThrottleCommand data) {
		parentBroker.sendOnPort("mixThrottlesControl.throttleCommandOut", data);
	}

	/**
	 * Method that is called by the AsyncEventHandler in order to map the dispatch event of an AADL thread.
	 */
	private final void dispatch() {

		pitchThrottleRequestIn.receiveInput();
		headingThrottleRequestIn.receiveInput();

		shutdownCommandIn.receiveInput();

		rollThrottleRequestIn.receiveInput();

		altitudeThrottleRequestIn.receiveInput();
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
		parentBroker.sendOnConnection("con_22", throttleCommandOut.getFWData());
		parentBroker.sendOnConnection("specialCon2", throttleCommandOut.getFWData());
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
