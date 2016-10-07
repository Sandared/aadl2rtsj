package xplane.parser.exceptions;

/**
 * Indicates that an invalid packet type was supplied to a parser.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 * @see simulation.parser.factories.PacketFactory
 */
public class UnknownPacketTypeException extends ParseException {
	private static final long serialVersionUID = -791829441702439068L;

	/**
	 * Create a new {@link UnknownPacketTypeException} with the specified error
	 * message
	 *
	 * @param message the error message
	 */
	public UnknownPacketTypeException(String message) {
		super(message);
	}
}
