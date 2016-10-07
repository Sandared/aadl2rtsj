package xplane.parser.exceptions;

/**
 * Indicates that a packet supplied to a parser contains invalid data and cannot
 * be parsed successfully.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 * @see simulation.parser.factories.PacketFactory
 */
public class MalformedPacketException extends ParseException {
	private static final long serialVersionUID = -164807672650181730L;

	/**
	 * Create a new {@link MalformedPacketException} with the specified error
	 * message
	 *
	 * @param message the error message
	 */
	public MalformedPacketException(String message) {
		super(message);
	}
}
