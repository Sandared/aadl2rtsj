package instance.autopilot_impl.autopilot.rollcontrol;

import java.util.Map;
import java.util.HashMap;

//########## Classifier Imports ##########
import classifier.autopilot.external.request.RequestThrottleRequest;
import classifier.autopilot.external.rollcontrol.RollControlimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.BasicConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

/**
 * The standard ConnectionBroker to be used by components with inner connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class rollControlConnectionBroker extends BasicConnectionBroker {

	/**
	 * A logger for logging errors/warnings/etc.
	 */
	private static final Logger log = Logger.getLogger(rollControlConnectionBroker.class.getName());

	/**
	 * A map of synchronisation Objects. For each subcomponent that is the ultimative source of a connection</br>
	 * where a syncObject is needed (immediate connection), a syncObject is created lazily and stored in this Map.
	 */
	private Map<String, Object> syncObjects = new HashMap<String, Object>();

	/**
	 * Broker to be used when retrieving syncObjects from 'higher' nested components
	 */
	private ConnectionBroker parentBroker;

	/**
	 * Component whose out ports are to be used when forwarding connections to 'higher' nested components
	 */
	private RollControlimpl component;

	public rollControlConnectionBroker(ConnectionBroker parentBroker, RollControlimpl component) {
		this.parentBroker = parentBroker;
		this.component = component;
	}

	@Override
	public void sendOnConnection(String connection, Object data) {
		switch (connection) {
		default:
			log.severe("No connection to send on found for: " + connection);
			break;
		}
	}

	@Override
	public void sendOnPort(String port, Object data) {
		switch (port) {
		default:
			log.severe("No port to send on found for: " + port);
			break;
		}
	}

	@Override
	public Object getSynchronisationObjectForConnection(String connection) {
		switch (connection) {
		default:
			log.severe("No sync object found for connectionname: " + connection);
			return null;
		}
	}

	@Override
	public Object getSynchronisationObjectForPort(String port) {
		switch (port) {
		default:
			log.severe("No sync object found for portname: " + port);
			return null;
		}
	}
}
