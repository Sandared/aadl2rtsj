package bus.api;

/**
 * Represents a local broadcast message bus. Listeners can either be
 * automatically registered using the OSGi whiteboard pattern by implementing
 * the {@link BusListener} interface, or can be manually added and removed using
 * the {@link #registerListener(BusListener)} and
 * {@link #unregisterListener(BusListener)} methods.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 *
 */
public interface Bus {
	/**
	 * Manually registers a new listener to this message bus. If the listener is
	 * already registered on this bus, this operation has no further effect.
	 *
	 * @param listener
	 *            the listener to register, must not be null
	 */
	void registerListener(BusListener listener);

	/**
	 * Manually removes a previously registered listener from this message bus.
	 * If the specified listener is {@code null} or has not been previously
	 * registered, an exception is thrown.
	 *
	 * @param listener
	 *            the listener to remove from this bus, must not be null
	 *
	 * @throws IllegalStateException
	 *             if the specified listener has not been previously registered
	 */
	void unregisterListener(BusListener listener);

	/**
	 * Sends a broadcast message to all registered listeners on this bus.
	 *
	 * @param message
	 *            the message to broadcast, must not be null
	 */
	void broadcast(BusMessage message);
}
