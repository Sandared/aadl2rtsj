package instance.autopilot_impl.autopilot.positioncontrol;

import javax.realtime.Timer;
import javax.realtime.OneShotTimer;
import javax.realtime.RelativeTime;
import javax.realtime.PeriodicTimer;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PriorityParameters;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandAltitudeCommand;
import classifier.autopilot.external.command.CommandHeadingCommand;
import classifier.autopilot.external.command.CommandPitchCommand;
import classifier.autopilot.external.command.CommandRollCommand;
import classifier.autopilot.external.command.CommandShutdownCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.positioncontrol.PositionControlimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

//########## Usercode Import ##########
import user.autopilot_impl.autopilot.positioncontrol.positionControlUserCode;

/**
 * This class represents the instance model element positionControl.</br>
 * It defines a ConnectionBroker for the handling of connections defined within positionControl and a parent ConnectionBroker to forward or initiate data transfer into 'higher' nested components.</br>
 * It defines am out'dataPortName' method for each OUT DATA PORT, which broadcasts the given data over the parentBroker on all outgoing connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class positionControl extends PositionControlimpl {
	/**
	 * The Logger to log information on
	 */
	private static final Logger log = Logger.getLogger(positionControl.class.getName());

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
	private positionControlUserCode userCode = new positionControlUserCode(positionInformationIn.getUserInPort(),
			positionCommandIn.getUserInPort(), shutdownCommandIn.getUserInPort(), altitudeCommandOut.getUserOutPort(),
			pitchCommandOut.getUserOutPort(), rollCommandOut.getUserOutPort(), headingCommandOut.getUserOutPort(),
			positionInformationOut.getUserOutPort(), shutdownCommandOut.getUserOutPort());

	public positionControl() {
	}

	//########## Inherited Methods from ConnectionBrokerable ##########
	@Override
	public void setParentConnectionBroker(ConnectionBroker parentBroker) {
		this.parentBroker = parentBroker;
		broker = new positionControlConnectionBroker(parentBroker, this);
	}

	@Override
	public ConnectionBroker getConnectionBroker() {
		return broker;
	}

	//########## Inherited Methods from  PositionControlimpl##########
	@Override
	public void outAltitudeCommandOut(CommandAltitudeCommand data) {
		parentBroker.sendOnPort("positionControl.altitudeCommandOut", data);
	}

	@Override
	public void outPitchCommandOut(CommandPitchCommand data) {
		parentBroker.sendOnPort("positionControl.pitchCommandOut", data);
	}

	@Override
	public void outRollCommandOut(CommandRollCommand data) {
		parentBroker.sendOnPort("positionControl.rollCommandOut", data);
	}

	@Override
	public void outHeadingCommandOut(CommandHeadingCommand data) {
		parentBroker.sendOnPort("positionControl.headingCommandOut", data);
	}

	@Override
	public void outPositionInformationOut(InformationPositionInformation data) {
		parentBroker.sendOnPort("positionControl.positionInformationOut", data);
	}

	@Override
	public void outShutdownCommandOut(CommandShutdownCommand data) {
		parentBroker.sendOnPort("positionControl.shutdownCommandOut", data);
	}

	/**
	 * Method that is called by the AsyncEventHandler in order to map the dispatch event of an AADL thread.
	 */
	private final void dispatch() {

		shutdownCommandIn.receiveInput();
		positionInformationIn.receiveInput();

		positionCommandIn.receiveInput();
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
		parentBroker.sendOnConnection("con_17", headingCommandOut.getFWData());
		parentBroker.sendOnConnection("con_14", shutdownCommandOut.getFWData());
		parentBroker.sendOnConnection("con_15", shutdownCommandOut.getFWData());
		parentBroker.sendOnConnection("con_12", shutdownCommandOut.getFWData());
		parentBroker.sendOnConnection("con_13", shutdownCommandOut.getFWData());

		parentBroker.sendOnConnection("con_6", altitudeCommandOut.getFWData());

		parentBroker.sendOnConnection("con_7", rollCommandOut.getFWData());

		parentBroker.sendOnConnection("con_9", positionInformationOut.getFWData());
		parentBroker.sendOnConnection("con_11", positionInformationOut.getFWData());
		parentBroker.sendOnConnection("con_8", positionInformationOut.getFWData());
		parentBroker.sendOnConnection("con_10", positionInformationOut.getFWData());

		parentBroker.sendOnConnection("con_16", pitchCommandOut.getFWData());
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
