package xplane;

import bus.api.BusMessage;
import bus.api.ThrottleCommand;
import xplane.data.XplaneDataRef;

/**
 * Helper class to convert bus messages into X-Plane datarefs.
 * <p>
 * <b>Note</b>: Not every bus message can be converted. At the moment, only
 * outgoing commands are supported by this class.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class BusMessageMarshaller {
	/**
	 * Marshal an arbitrary bus message into an equivalent X-Plane dataref.
	 *
	 * @param message
	 *            The bus message to marshal
	 * @return An {@link XplaneDataRef} equivalent to the bus message, or
	 *         {@code null}, if the type of the bus message is not supported
	 */
	public XplaneDataRef mashal(BusMessage message) {
		switch (message.getType()) {
		case ThrottleCommand:
			return marshal((ThrottleCommand) message);

		default:
			return null;
		}
	}

	private static XplaneDataRef marshal(ThrottleCommand cmd) {
		final int engineNum = cmd.getEngineNum();
		final String path = String.format("sim/cockpit2/engine/actuators/throttle_ratio[%d]", engineNum);
		return new XplaneDataRef(path, cmd.getThrottle());
	}
}
