package classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.floatdata;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.DeepCopyable;

public class FloatData implements DeepCopyable<FloatData> {

	/**
	 * A member variable to store the Float value of this BaseType
	 */
	private Float value;

	/**
	 * @return the Float value of this FloatData
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value the new Float value tfor this FloatData
	 */
	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public FloatData deepCopy() {
		FloatData result = new FloatData();
		if (this.value != null) {
			result.value = new Float(this.value);
		}
		return result;
	}
}
