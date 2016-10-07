package de.uniaugsburg.smds.aadl2rtsj.generation.util;

/**
 * Takes care of intra- and inter-component communication. 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface ConnectionBroker {

	/**
	 * Sends the given data over the given connection.</br>
	 * Only connections within the component of this ConnectionsBroker can be adressed.</br> 
	 * Usually used to initiate communication. If one is just forwarding a deeper nested connection, then the {@link #sendOnPort sendOnPort(String port, Object data)} method should be used.</br>
	 * The name of the connection is unique in the context of a component, so it serves as a unique identifier.
	 * @param connection the name of the connection to send on
	 * @param data the data to be sent over the connection
	 */
	void sendOnConnection(String connection, Object data);

	/**
	 * Sends the given data over the given port.</br>
	 * Only ports of subcomponents of this ConnectionBroker's component can be adressed, which is why the port name is concatenated with the subcomponent's name in order to be unique.</br>
	 * This is basically a broadcast on all outgoing connections of this port.</br>
	 * Usually used to forward data from a deeper nested connection. If one wants to initiate communication, then the {@link #sendOnConnection sendOnConnection(String connection, Object data)} method should be used.</br>
	 * @param port the name of the port to send on, concatenated with the subcomponent's name
	 * @param data the data to be sent over the port
	 */
	void sendOnPort(String port, Object data);

	/**
	 * In order to be able to forward communication into deeper nested components, this mehtod adds the respective child ConnectionBroker.</br>
	 * @param childName the name of the subcomponent the broker is added for
	 * @param broker the ConnectionBroker to be added for the given childName
	 */
	void addChildBroker(String childName, ConnectionBroker broker);

	/**
	 * Usually used by the receiving thread to acquire a syncObject for a sepcific connection.</br>
	 * Either the request is forwarded to the next ConnectionBroker, if there is a fitting connection, or a new syncObject is created saved and returned.
	 * SyncObjects are used to implement immediate connections and their semantic.</br> 
	 * For each immediate connection there is exactly one syncObject both parties can sync on.</br>
	 * @param connection the name of the connection one wants a syncObject for.
	 */
	Object getSynchronisationObjectForConnection(String connection);

	/**
	 * Usually used by the receiving thread to acquire a syncObject for a sepcific port.</br>
	 * IN DATA PORTS usually have only one incoming connection (we exclude data aggregation and modes), thus a call to this port is forwarded to the respective {@link #getSynchronisationObjectForConnection getSynchronisationObjectForConnection(String connection)}.</br>
	 * SyncObjects are used to implement immediate connections and their semantic.</br> 
	 * For each immediate connection there is exactly one syncObject both parties can sync on.</br>
	 */
	Object getSynchronisationObjectForPort(String port);
}
