package bus.api;

/**
 * Marker interface for a message class that can be transmitted over a message
 * bus
 *
 * @see api.services.Bus
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public interface BusMessage {
	/**
	 * @return the type of this bus message
	 */
	BusMessageType getType();
}
