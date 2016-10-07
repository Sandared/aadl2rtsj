package instance.autopilot_impl;

//########## Classifier Imports ##########
import classifier.autopilot.external.autopilot.autopilotimpl;
import classifier.autopilot.external.autopilotprocess.autopilotProcessimpl;
import classifier.autopilot.external.x_plane.x_planeimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

/**
 * This class represents the instance model element autopilot_impl_Instance.</br>
 * It defines a ConnectionBroker for the handling of connections defined within autopilot_impl_Instance and a parent ConnectionBroker to forward or initiate data transfer into 'higher' nested components.</br>
 * It defines am out'dataPortName' method for each OUT DATA PORT, which brodcasts the given data over the parentBroker on all outgoing connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class autopilot_impl_Instance extends autopilotimpl {
	private static final Logger log = Logger.getLogger(autopilot_impl_Instance.class.getName());

	/**
	 * ConnectionBroker to send on outgoing connections. Has to be set by the parent component.
	 */
	private ConnectionBroker parentBroker;

	/**
	 * ConnectionBroker for the connections within this Thread
	 */
	private ConnectionBroker broker;

	public autopilot_impl_Instance(x_planeimpl simulation, autopilotProcessimpl autopilot) {
		this.simulation = simulation;
		this.autopilot = autopilot;
	}

	//########## Inherited Methods from ConnectionBrokerable ##########
	@Override
	public void setParentConnectionBroker(ConnectionBroker parentBroker) {
		this.parentBroker = parentBroker;
		broker = new autopilot_impl_InstanceConnectionBroker(parentBroker, this, simulation, autopilot);
		broker.addChildBroker("simulation", simulation.getConnectionBroker());
		simulation.setParentConnectionBroker(broker);
		broker.addChildBroker("autopilot", autopilot.getConnectionBroker());
		autopilot.setParentConnectionBroker(broker);
	}

	@Override
	public ConnectionBroker getConnectionBroker() {
		return broker;
	}

}
