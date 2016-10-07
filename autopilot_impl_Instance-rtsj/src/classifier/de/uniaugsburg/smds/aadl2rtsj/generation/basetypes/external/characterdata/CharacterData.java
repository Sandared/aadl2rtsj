package classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.characterdata;

import de.uniaugsburg.smds.aadl2rtsj.generation.util.DeepCopyable;

public class CharacterData implements DeepCopyable<CharacterData> {

	/**
	 * A member variable to store the Character value of this BaseType
	 */
	private Character value;

	/**
	 * @return the Character value of this CharacterData
	 */
	public Character getValue() {
		return value;
	}

	/**
	 * @param value the new Character value tfor this CharacterData
	 */
	public void setValue(Character value) {
		this.value = value;
	}

	@Override
	public CharacterData deepCopy() {
		CharacterData result = new CharacterData();
		if (this.value != null) {
			result.value = new Character(this.value);
		}
		return result;
	}
}
