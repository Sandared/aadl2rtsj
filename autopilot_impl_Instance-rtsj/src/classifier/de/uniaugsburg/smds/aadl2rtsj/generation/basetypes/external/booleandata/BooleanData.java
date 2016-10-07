package classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.booleandata;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.DeepCopyable;

public class BooleanData implements DeepCopyable<BooleanData> {

	/**
	 * A member variable to store the Boolean value of this BaseType
	 */
	private Boolean value;

	/**
	 * @return the Boolean value of this BooleanData
	 */
	public Boolean getValue() {
		return value;
	}

	/**
	 * @param value the new Boolean value tfor this BooleanData
	 */
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public BooleanData deepCopy() {
		BooleanData result = new BooleanData();
		if (this.value != null) {
			result.value = new Boolean(this.value);
		}
		return result;
	}
}
