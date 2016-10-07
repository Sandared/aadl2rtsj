package instance.autopilot_impl.simulation;

//########## Classifier Imports ##########
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.x_plane.x_planeimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

// Start of user code device imports
import classifier.autopilot.external.command.CommandThrottleCommand;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.OutPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.InPort;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.NoDataException;

import javax.realtime.PriorityParameters;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicTimer;
import javax.realtime.RelativeTime;
import javax.realtime.Timer;
import bus.MyBusListener;
import bus.api.Bus;

// End of user code
/**
 * This class represents the instance model element simulation.</br>
 * It defines a ConnectionBroker for the handling of connections defined within simulation and a parent ConnectionBroker to forward or initiate data transfer into 'higher' nested components.</br>
 * It defines am out'dataPortName' method for each OUT DATA PORT, which brodcasts the given data over the parentBroker on all outgoing connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class simulation extends x_planeimpl {
	private static final Logger log = Logger.getLogger(simulation.class.getName());

	/**
	 * ConnectionBroker to send on outgoing connections. Has to be set by the parent component.
	 */
	private ConnectionBroker parentBroker;

	/**
	 * ConnectionBroker for the connections within this Thread
	 */
	private ConnectionBroker broker;

	public simulation() {
	}

	//########## Inherited Methods from ConnectionBrokerable ##########
	@Override
	public void setParentConnectionBroker(ConnectionBroker parentBroker) {
		this.parentBroker = parentBroker;
		broker = new simulationConnectionBroker(parentBroker, this);
	}

	@Override
	public ConnectionBroker getConnectionBroker() {
		return broker;
	}

	@Override
	public void outPosition(InformationPositionInformation data) {
		parentBroker.sendOnPort("simulation.position", data);
	}

	// Start of user code device
	private MyBusListener listener;

	/**
	 * The AsyncEventHandler which makes this class an executable entity
	 */
	private InnerAsyncEventHandler handler = new InnerAsyncEventHandler();

	/**
	 * The timer, that triggers the AsyncEventHandler in a periodic manner
	 */
	private Timer timer = new PeriodicTimer(new RelativeTime(), new RelativeTime(50, 0), handler);

	class InnerAsyncEventHandler extends AsyncEventHandler {
		public InnerAsyncEventHandler() {
			setDaemon(false);
			setSchedulingParameters(new PriorityParameters(5));
		}

		@Override
		public void handleAsyncEvent() {
			// freeze input
			throttle.receiveInput();
			//write newest throttle command to bus
			InPort<CommandThrottleCommand> userInPort = throttle.getUserInPort();
			CommandThrottleCommand throttle;
			try {
				throttle = userInPort.getData();
				listener.broadcastThrottle(throttle);
			} catch (NoDataException e) {
				//log.info("No data on in port");
			}

			//read newest position information from bus
			OutPort<InformationPositionInformation> userOutPort = position.getUserOutPort();
			userOutPort.setData(listener.getPositionInformation());

			// send data to autopilot
			parentBroker.sendOnConnection("posCon", position.getFWData());
		}
	}

	public void start(Bus bus, MyBusListener listener) {
		this.listener = listener;
		timer.start();
	}
	// End of user code
}
