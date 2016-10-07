package bus.api;

/**
 * Stores information about the aircraft's current spatial situation.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class PositionInformation implements BusMessage {
	/** Magnetic heading in deg */
	private float magHeading;

	/** True heading in deg */
	private float heading;

	/** Pitch angle in deg */
	private float pitch;

	/** Pitch angle in deg */
	private float roll;

	/** Roll rate in deg/sec */
	private float rollRate;

	/** Pitch rate in deg/sec */
	private float pitchRate;

	/** Yaw rate in deg/sec */
	private float yawRate;

	/** Vertical velocity indicator reading in ft/min */
	private float vvi;

	/** Indicated airspeed in kn */
	private float ias;

	/** Angle of attack in deg */
	private float angleOfAttack;

	/** Angle of side-slip in deg */
	private float angleOfSideslip;

	/** Geographical location, including altitude */
	private GeoCoordinate location;

	@Override
	public BusMessageType getType() {
		return BusMessageType.Position;
	}

	public float getMagHeading() {
		return magHeading;
	}

	public void setMagHeading(float magHeading) {
		this.magHeading = magHeading;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getRollRate() {
		return rollRate;
	}

	public void setRollRate(float rollRate) {
		this.rollRate = rollRate;
	}

	public float getPitchRate() {
		return pitchRate;
	}

	public void setPitchRate(float pitchRate) {
		this.pitchRate = pitchRate;
	}

	public float getYawRate() {
		return yawRate;
	}

	public void setYawRate(float yawRate) {
		this.yawRate = yawRate;
	}

	public float getVvi() {
		return vvi;
	}

	public void setVvi(float vvi) {
		this.vvi = vvi;
	}

	public float getIas() {
		return ias;
	}

	public void setIas(float speed) {
		this.ias = speed;
	}

	public float getAngleOfAttack() {
		return angleOfAttack;
	}

	public void setAngleOfAttack(float angleOfAttack) {
		this.angleOfAttack = angleOfAttack;
	}

	public float getAngleOfSideslip() {
		return angleOfSideslip;
	}

	public void setAngleOfSideslip(float angleOfSideslip) {
		this.angleOfSideslip = angleOfSideslip;
	}

	public GeoCoordinate getLocation() {
		return location;
	}

	public void setLocation(GeoCoordinate location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return String.format(
				"[loc=%s, hdg=%.2f, magHdg=%.2f, speed=%.2f, pitch=%.2f, roll=%.2f, alpha=%.3f, beta=%.3f]",
				location != null ? location.toString() : "", heading, magHeading, ias, pitch, roll, angleOfAttack,
				angleOfSideslip);
	}
}
