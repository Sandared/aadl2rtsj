package de.uniaugsburg.smds.aadl2rtsj.generation.util;

/**
 * An IN port meant for use by user code
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface InPort<T> {

	/**
	 * @return true if the data for this port has been changed since the last call to isDirty()
	 */
	boolean isDirty();

	/**
	 * @return the latest data stored in this port or throws an NoDataException if the data would be null
	 */
	T getData() throws NoDataException;
}
