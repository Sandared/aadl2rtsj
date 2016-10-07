package de.uniaugsburg.smds.aadl2rtsj.generation.util;

/**
 * An IN/OUT port meant for use by user code
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface InOutPort<T> {

	/**
	 * @return true if the data for this port has been changed since the last call to isDirty()
	 */
	boolean isDirty();

	/**
	 * Sets the data for this port to send on the timings, determined by the generated code.
	 */
	public void setData(T data);

	/**
	 * @return the latest data stored in this port or throws an NoDataException if the data would be null
	 */
	T getData() throws NoDataException;
}
