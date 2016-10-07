package xplane.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * An X-Plane DataRef packet
 * <p/>
 * DataRefs encapsulate information about a single variable within the
 * simulation. They are identified by their {@link #path} and carry a single
 * float {@link #value}.
 * <p/>
 * A list of available DataRefs can be found in the
 * <a href="http://www.xsquawkbox.net/xpsdk/docs/DataRefs.html">X-Plane SDK
 * wiki</a>.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class XplaneDataRef {
	private static final Charset UTF8 = Charset.forName("UTF-8");

	// Expected length of an X-Plane UDP input message in bytes
	private static final int PACKET_LENGTH = 509;

	private final String path;
	private float value;

	/**
	 * Create a new DataRef packet without a value
	 *
	 * @param path
	 *            the DataRef path
	 */
	public XplaneDataRef(String path) {
		this.path = path;
	}

	/**
	 * Create a new DataRef packet with a defined value
	 *
	 * @param path
	 *            the DataRef path
	 * @param value
	 *            the value for the DataRef
	 */
	public XplaneDataRef(String path, float value) {
		this.path = path;
		this.value = value;
	}

	/**
	 * @return the DataRef path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the DataRef value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Set the new value for this DataRef
	 * 
	 * @param value
	 *            the new DataRef value
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * Serialize this DataRef packet for transmission over the X-Plane UDP
	 * interface
	 * 
	 * @return a buffer containing the serialized DataRef packet
	 */
	public ByteBuffer serialize() {
		final ByteBuffer buf = ByteBuffer.allocate(PACKET_LENGTH);

		buf.put((byte) 'D');
		buf.put((byte) 'R');
		buf.put((byte) 'E');
		buf.put((byte) 'F');
		buf.put((byte) 0);

		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.putFloat(value);

		buf.put(UTF8.encode(path));

		buf.rewind();
		return buf;
	}

	@Override
	public String toString() {
		return String.format("[DataRef, path=%s, value=%.2f]", path, value);
	}
}
