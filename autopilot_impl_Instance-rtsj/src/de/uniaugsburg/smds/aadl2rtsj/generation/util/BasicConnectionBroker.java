package de.uniaugsburg.smds.aadl2rtsj.generation.util;

import java.util.Map;
import java.util.HashMap;

/**
 * A basic implementation for a ConnectionBroker. Merely takes care of child brokers and makes them available to extending classes.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class BasicConnectionBroker implements ConnectionBroker {
	/**
	 * Contains all brokers of this ConecctionBroker's component's subcomponents
	 */
	protected Map<String, ConnectionBroker> childBrokers = new HashMap<String, ConnectionBroker>();

	@Override
	public void addChildBroker(String childName, ConnectionBroker broker) {
		childBrokers.put(childName, broker);
	}
}
