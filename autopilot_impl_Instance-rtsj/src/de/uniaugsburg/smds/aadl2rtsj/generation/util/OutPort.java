package de.uniaugsburg.smds.aadl2rtsj.generation.util;

/**
 * An OUT port meant for use by user code
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface OutPort<T> {

	/**
	 * @return true if the data for this port has been changed since the last call to isDirty()
	 */
	boolean isDirty();

	/**
	 * Sets the data for this port to send on the timings, determined by the generated code.
	 */
	void setData(T data);
}
