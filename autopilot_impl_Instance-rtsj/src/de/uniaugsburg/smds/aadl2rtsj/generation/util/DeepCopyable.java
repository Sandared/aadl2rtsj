package de.uniaugsburg.smds.aadl2rtsj.generation.util;

/**
 * This Interface enables a deep copy of the implementing class.</br>
 * Usually used by Data Components in order to be 'freezeable' for a dispatch cycle of a thread.
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public interface DeepCopyable<T> {

	/**
	 * Copies the Object and all its members into a completely new object.
	 * @return a deep copy (not shallow!) of this Object 
	 */
	T deepCopy();
}
