package de.uniaugsburg.smds.aadl2rtsj.generation.util;

/**
 * A generic class for IN/OU/INOUT DATA PORTS. Only meant for use by the generated code. Users get one of the inner port classes for further use
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public class DataPort<T> {

	/**
	 * A member variable to store the outgoing data for this port. Only for use by the generated code
	 */
	private volatile T outFWData = null;

	/**
	 * A member variable to store the incoming data for this port. Only for use by the generated code
	 */
	private volatile T inFWData = null;

	/**
	 * A member variable to store the status of data for this port. Only for use by the user code</br>
	 * Gets resetted each dispatch cycle by the generated code
	 */
	private volatile boolean isDirty = false;

	/**
	 * A member variable to store the outgoing data for this port. This data is made accessible to the user by one of the inner classes
	 */
	private volatile T outData = null;

	/**
	 * A member variable to freeze the incoming data into for this port. This data is made accessible to the user by one of the inner classes
	 */
	private volatile T inData = null;

	/**
	 * A member variable to grant user code restricted access to outData
	 */
	private OutPort<T> outPort = new InnerOutPort<T>();

	/**
	 * A member variable to grant user code restricted access to inData
	 */
	private InPort<T> inPort = new InnerInPort<T>();

	/**
	 * A member variable to grant user code restricted access to inData and outData
	 */
	private InOutPort<T> inOutPort = new InnerInOutPort<T>();

	/**
	 * Sets the data for this port as the newest data. Old data is replaced by incoming data.</br>
	 * The status of this port is set to dirty, as the available data has changed.
	 * @param data the data to be set in this data port as newest data
	 */
	public void setFWData(T data) {
		inFWData = data;
		isDirty = true;
	}

	/**
	 * Gets the data that was made available by usercode for this port. Old data is replaced by the newest data from the usercode.</br>
	 * @return the newest data made available by usercode
	 */
	public synchronized T getFWData() {
		outFWData = outData;
		return outFWData;
	}

	/**
	 * Freezes the newest incoming data of this port for further use by user code.</br>
	 * The frozen data won't change until the next call to receiveInput.</br>
	 * The status of this port is set to clean.
	 */
	public void receiveInput() {
		//If the data is null do nothing, the value for the user will then be either null or the last value
		if (inFWData != null)
			inData = (T) ((DeepCopyable) inFWData).deepCopy();
	}

	/**
	 * @return true if the data for this port has been changed since the last call to isDirty()
	 */
	public boolean isFWDirty() {
		// store the old value for later return
		boolean oldDirty = isDirty;
		// set isDirty to false for the next caller
		isDirty = false;
		return oldDirty;
	}

	/**
	 * Gets a restricted view on this port's outgoing data for further use in usercode.</br>
	 * @return an OUT PORT view on this ports data
	 */
	public OutPort<T> getUserOutPort() {
		return outPort;
	}

	/**
	 * Gets a restricted view on this port's incoming, frozen data for further use in usercode.</br>
	 * @return an IN PORT view on this ports data
	 */
	public InPort<T> getUserInPort() {
		return inPort;
	}

	/**
	 * Gets a restricted view on this port's ingoing, frozen and outgoing data for further use in usercode.</br>
	 * @return an IN OUT PORT view on this ports data
	 */
	public InOutPort<T> getUserInOutPort() {
		return inOutPort;
	}

	public class InnerOutPort<X extends T> implements OutPort<X> {
		@Override
		public boolean isDirty() {
			return isFWDirty();
		}

		@Override
		public void setData(X data) {
			outData = data;
		}
	}

	public class InnerInPort<X extends T> implements InPort<X> {
		@Override
		public boolean isDirty() {
			return isFWDirty();
		}

		@Override
		public synchronized X getData() throws NoDataException {
			if (inData == null)
				throw new NoDataException();
			return (X) inData;
		}
	}

	public class InnerInOutPort<X extends T> implements InOutPort<X> {
		@Override
		public boolean isDirty() {
			return isFWDirty();
		}

		@Override
		public synchronized X getData() throws NoDataException {
			inData = inFWData;
			if (inData == null)
				throw new NoDataException();
			return (X) inData;
		}

		@Override
		public void setData(X data) {
			outData = data;
		}
	}
}
