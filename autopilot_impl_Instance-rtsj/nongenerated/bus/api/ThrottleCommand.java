package bus.api;

/**
 * Represents a bus message to command the aircraft's throttle setting.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class ThrottleCommand implements BusMessage {
	/**
	 * Numeric throttle lever identifier, corresponds to the engine with the
	 * same identifier
	 */
	private final int engineNum;

	/** Throttle level, from 0.0 (idle) to 1.0 (full throttle) */
	private final float throttle;

	/**
	 * Constructs a new instance of the throttle command message.
	 *
	 * @param engineNum
	 *            the numeric throttle lever identifier, corresponds to the
	 *            engine with the same identifier
	 * @param throttle
	 *            the throttle setting, from 0.0 (idle) to 1.0 (full throttle)
	 *
	 * @throws IllegalArgumentException
	 *             if the specified throttle setting lies outside the allowed
	 *             range
	 */
	public ThrottleCommand(int engineNum, float throttle) {
		if (throttle < 0f || throttle > 1.0f) {
			throw new IllegalArgumentException("");
		}

		this.engineNum = engineNum;
		this.throttle = throttle;
	}

	@Override
	public BusMessageType getType() {
		return BusMessageType.ThrottleCommand;
	}

	/**
	 * @return Engine number
	 */
	public int getEngineNum() {
		return engineNum;
	}

	/**
	 * @return Throttle setting in %
	 */
	public float getThrottle() {
		return throttle;
	}

	@Override
	public String toString() {
		return String.format("[ThrottleCommand engine #%d, throttle=%.3f]", engineNum, throttle);
	}
}
