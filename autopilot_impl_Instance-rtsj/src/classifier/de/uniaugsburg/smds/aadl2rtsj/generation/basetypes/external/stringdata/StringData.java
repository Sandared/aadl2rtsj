package classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.stringdata;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.DeepCopyable;

public class StringData implements DeepCopyable<StringData> {

	/**
	 * A member variable to store the String value of this BaseType
	 */
	private String value;

	/**
	 * @return the String value of this StringData
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the new String value tfor this StringData
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public StringData deepCopy() {
		StringData result = new StringData();
		if (this.value != null) {
			result.value = new String(this.value);
		}
		return result;
	}
}
