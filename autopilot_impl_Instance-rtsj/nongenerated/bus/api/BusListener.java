package bus.api;

/**
 * A BusListener can receive notifications for messages that are broadcast on a
 * bus.
 * <p>
 * No explicit registration of the listener is necessary, instead the OSGi
 * whiteboard pattern is used to register all newly created listener instances
 * with the respective message bus.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public interface BusListener {
	/**
	 * Notify the listener of a bus message.
	 * 
	 * @param message
	 *            the message being broadcast on the bus
	 */
	void processMessage(BusMessage message);
}
