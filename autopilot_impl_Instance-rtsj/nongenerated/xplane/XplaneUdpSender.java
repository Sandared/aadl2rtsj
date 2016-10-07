package xplane;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.logging.Logger;

import bus.api.BusListener;
import bus.api.BusMessage;
import bus.api.ThrottleCommand;
import common.Configuration;
import xplane.data.XplaneDataRef;

/**
 * This class manages outgoing connections with a running X-Plane 10 instance.
 *
 * It permits sending both simulation commands (CMND packets) as well as setting
 * datarefs (DREF packets) inside the simulation. Input are received either via
 * a {@link BusMessage} received on the common message bus or by direct
 * invocation of the {@link #sendCommand(XplaneCommand)} and
 * {@link #sendDataRef(XplaneDataRef)} methods.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class XplaneUdpSender implements BusListener {
	private static final Logger log = Logger.getLogger(XplaneUdpSender.class.getName());
	private static final BusMessageMarshaller marshaller = new BusMessageMarshaller();

	private DatagramChannel outChannel;
	private volatile Configuration config;

	public void setConfiguration(Configuration config) {
		this.config = config;
	}

	/**
	 * Receives a message from the common message bus and processes both
	 * simulation commands and datarefs. All other messages are silently
	 * discarded and ignored.
	 */
	@Override
	public void processMessage(BusMessage message) {
		try {
			final XplaneDataRef dataRef = marshaller.mashal(message);

			if (dataRef != null) {
				sendDataRef(dataRef);
			}

		} catch (IOException ex) {
			log.warning("Could not transmit message " + ex);
		}
	}

	/**
	 * Directly sends a dataref to the connected X-Plane instance.
	 *
	 * Note: A UDP connection to X-Plane must have been established first by
	 * calling the {@link #init()} method, otherwise this method will throw an
	 * exception.
	 *
	 * @param dataRef
	 *            the X-Plane dataref to update in the simulation
	 *
	 * @throws IOException
	 *             if the UDP communication with X-Plane resulted in an error
	 * @throws IllegalStateException
	 *             if the UDP connection to X-Plane has not been previously
	 *             established
	 */
	public synchronized void sendDataRef(XplaneDataRef dataRef) throws IOException {
		//        if (log.isTraceEnabled()) {
		//            log.trace("sendDataRef: " + dataRef);
		//        }

		if (!outChannel.isConnected()) {
			throw new IllegalStateException(
					"No UDP connection to X-Plane has been established, must call init() first.");
		}
		outChannel.write(dataRef.serialize());
	}

	/**
	 * Enables this instance of the UDP data sender.
	 *
	 * This operation must be called before any data can be transmitted to the
	 * X-Plane instance under control.
	 *
	 * @throws IOException
	 *             If no connection to the remote X-Plane instance can be
	 *             established
	 */
	public void init() throws IOException {
		final String xplaneHost = config.getXplaneHost();
		final int xplanePort = config.getXplaneOutPort();

		//        log.info("Opening outgoing X-Plane connection to {}:{}", xplaneHost,
		//                xplanePort);

		final SocketAddress targetAddress = new InetSocketAddress(InetAddress.getByName(xplaneHost), xplanePort);

		outChannel = DatagramChannel.open();
		outChannel.connect(targetAddress);
	}
}
