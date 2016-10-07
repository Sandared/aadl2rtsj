package common;

/**
 * Data class that stores system configuration values.
 * <p>
 * This class is intended to be (de-)serialized to JSON format.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class Configuration {
	private String xplaneHost = "192.168.56.1";
	private int xplaneInPort = 49003;
	private int xplaneOutPort = 49000;
	private int busUpdatePeriod = 100;

	/**
	 * @return Host name of the X-Plane instance to connect to
	 */
	public String getXplaneHost() {
		return xplaneHost;
	}

	/**
	 * @return UDP port on which to listen for incoming X-Plane data connections
	 */
	public int getXplaneInPort() {
		return xplaneInPort;
	}

	/**
	 * @return UDP port for outgoing data connections to X-Plane
	 */
	public int getXplaneOutPort() {
		return xplaneOutPort;
	}

	/**
	 * @return Update period for periodic broadcast of status messages on the
	 *         message bus in ms.
	 */
	public int getBusUpdatePeriod() {
		return busUpdatePeriod;
	}
}
