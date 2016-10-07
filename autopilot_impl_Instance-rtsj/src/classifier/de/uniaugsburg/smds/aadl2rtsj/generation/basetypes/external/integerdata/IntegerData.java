package classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.integerdata;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.DeepCopyable;

public class IntegerData implements DeepCopyable<IntegerData> {

	/**
	 * A member variable to store the Integer value of this BaseType
	 */
	private Integer value;

	/**
	 * @return the Integer value of this IntegerData
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param value the new Integer value tfor this IntegerData
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public IntegerData deepCopy() {
		IntegerData result = new IntegerData();
		if (this.value != null) {
			result.value = new Integer(this.value);
		}
		return result;
	}
}
