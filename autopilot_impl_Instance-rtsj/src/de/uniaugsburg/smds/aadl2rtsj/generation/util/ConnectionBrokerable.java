package de.uniaugsburg.smds.aadl2rtsj.generation.util;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.ConnectionBroker;

/**
 * Implementing this interface makes the class accessible to the ConnectionBroker mechanisms, that enable inter- and intra-component communication.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface ConnectionBrokerable {

	/**
	 * Sets the parent broker of this component. Each component can have exactly one parent and thus only one parent broker.</br>
	 * Usually the parent broker is used to forward connections into 'higher' nested components or to initiate communication,</br>
	 * if this component is the ultimative source of a connection.
	 * @param broker the parent broker to be set
	 */
	void setParentConnectionBroker(ConnectionBroker broker);

	/**
	 * Usually used by parent components to achieve access to subcomponent's borkers in order to forward connections into 'deeper' nested components
	 * @return the ConnectionBroker of this component.
	 */
	ConnectionBroker getConnectionBroker();
}
