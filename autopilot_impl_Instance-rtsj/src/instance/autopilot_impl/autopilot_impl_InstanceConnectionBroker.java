package instance.autopilot_impl;

import java.util.Map;
import java.util.HashMap;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;
import classifier.autopilot.external.autopilot.autopilotimpl;
import classifier.autopilot.external.autopilotprocess.autopilotProcessimpl;
import classifier.autopilot.external.x_plane.x_planeimpl;

//########## ConnectionBroker Import ###########
import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;
import de.uniaugsburg.smds.aadl2rtsj.generation.util.BasicConnectionBroker;

//########## Logger Import ##########
import java.util.logging.Logger;

/**
 * The standard ConnectionBroker to be used by components with inner connections.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class autopilot_impl_InstanceConnectionBroker extends BasicConnectionBroker {

	/**
	 * A logger for logging errors/warnings/etc.
	 */
	private static final Logger log = Logger.getLogger(autopilot_impl_InstanceConnectionBroker.class.getName());

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
	private autopilotimpl component;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private x_planeimpl simulation;

	/**
	 * Subcomponent whose IN ports are to be used when forwarding connections to 'deeper' nested components
	 */
	private autopilotProcessimpl autopilot;

	public autopilot_impl_InstanceConnectionBroker(ConnectionBroker parentBroker, autopilotimpl component,
			x_planeimpl simulation, autopilotProcessimpl autopilot) {
		this.parentBroker = parentBroker;
		this.component = component;
		this.simulation = simulation;
		this.autopilot = autopilot;
	}

	@Override
	public void sendOnConnection(String connection, Object data) {
		switch (connection) {
		case "throttleCon":
			// "throttle" is the target feature of "throttleCon" 
			// which is a connection between subcomponents of "autopilot_impl_Instance", 
			// so call its corresponding in method
			simulation.inThrottle((CommandThrottleCommand) data);
			break;
		case "posCon":
			// "positionIn" is the target feature of "posCon" 
			// which is a connection between subcomponents of "autopilot_impl_Instance", 
			// so call its corresponding in method
			autopilot.inPositionIn((InformationPositionInformation) data);
			break;
		default:
			log.severe("No connection to send on found for: " + connection);
			break;
		}
	}

	@Override
	public void sendOnPort(String port, Object data) {
		switch (port) {
		case "simulation.position":
			sendOnConnection("posCon", data);
			sendOnConnection("posCon", data);
			break;
		case "autopilot.throttleOut":
			sendOnConnection("throttleCon", data);
			break;
		default:
			log.severe("No port to send on found for: " + port);
			break;
		}
	}

	@Override
	public Object getSynchronisationObjectForConnection(String connection) {
		switch (connection) {
		case "posCon":
			// Look up the syncObject, as position which is the source feature of posCon 
			// has no ingoing connections within simulation (determined during generation).
			// If no existing syncObject is found, a new one is created and stored for later reuse.
			Object syncOnposCon = syncObjects.get("posCon");
			if (syncOnposCon == null) {
				syncOnposCon = new Object();
				syncObjects.put("posCon", syncOnposCon);
			}
			return syncOnposCon;
		case "throttleCon":
			// ask the childBroker for a syncObject, as throttleOut which is the source feature of throttleCon 
			// has ingoing connections within autopilot (determined during generation)
			return childBrokers.get("autopilot").getSynchronisationObjectForConnection("con_22");
		default:
			log.severe("No sync object found for connectionname: " + connection);
			return null;
		}
	}

	@Override
	public Object getSynchronisationObjectForPort(String port) {
		switch (port) {
		case "simulation.throttle":
			// throttle is the target of throttleCon. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on throttleCon for a syncObject.
			return getSynchronisationObjectForConnection("throttleCon");
		case "autopilot.positionIn":
			// positionIn is the target of posCon. As data ports have only one ingoing connection 
			// (no modes, no dataaggregation), we ask on posCon for a syncObject.
			return getSynchronisationObjectForConnection("posCon");
		default:
			log.severe("No sync object found for portname: " + port);
			return null;
		}
	}
}
