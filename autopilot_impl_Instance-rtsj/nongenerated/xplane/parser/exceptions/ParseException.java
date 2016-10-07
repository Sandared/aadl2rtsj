package xplane.parser.exceptions;

/**
 * Base class for all exceptions occuring during parsing of X-Plane network
 * packets.
 * 
 * @author Adrian Rumpold (a.rumpold@ds-lab.org)
 */
public abstract class ParseException extends Exception {
	private static final long serialVersionUID = 3406963119767185635L;

	/**
	 * Create a new {@link ParseException} with the specified error message
	 *
	 * @param message
	 *            the error message
	 */
	public ParseException(String message) {
		super(message);
	}
}
