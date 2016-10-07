package missionparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object that has all information for one MissionCommand Specific
 * parameters are exactly treated as in MissionPlanner
 * 
 * @author Manuel Hoss (manuel.hoss@hotmail.de)
 */
public class MissionCommand {

	private List<Float> commandFields;
	private int wayPointid;
	private MissionCommandType commandType;
	private float specificParameter1;
	private float specificParameter2;
	private float specificParameter3;
	private float specificParameter4;
	private float latitude;
	private float longitude;
	private float altitude;

	public MissionCommand() {

	}

	public List<Float> getCommandFields() {
		return new ArrayList<Float>(commandFields);
	}

	public void setCommandFields(List<Float> commandFields) {
		this.commandFields = new ArrayList<Float>(commandFields);
	}

	public int getWayPointid() {
		return wayPointid;
	}

	public void setWayPointid(int wayPointid) {
		this.wayPointid = wayPointid;
	}

	public MissionCommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(MissionCommandType commandType) {
		this.commandType = commandType;
	}

	/**
	 * Returns Loiter Time.
	 * 
	 * @return
	 */
	public float getSpecificParameter1() {
		return specificParameter1;
	}

	public void setSpecificParameter1(Float specificParameter1) {
		this.specificParameter1 = specificParameter1;
	}

	/**
	 * Returns range for skipping waypoint.
	 * 
	 * @return
	 */
	public float getSpecificParameter2() {
		return specificParameter2;
	}

	public void setSpecificParameter2(Float specificParameter2) {
		this.specificParameter2 = specificParameter2;
	}

	public float getSpecificParameter3() {
		return specificParameter3;
	}

	public void setSpecificParameter3(Float specificParameter3) {
		this.specificParameter3 = specificParameter3;
	}

	/**
	 * Returns Loiter Heading.
	 * 
	 * @return
	 */
	public float getSpecificParameter4() {
		return specificParameter4;
	}

	public void setSpecificParameter4(Float specificParameter4) {
		this.specificParameter4 = specificParameter4;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	@Override
	public String toString() {
		return "\nCommand number: " + wayPointid + "\nCommandType: " + commandType + "\nAlt: " + altitude + "\nLat: "
				+ latitude + "\nLong: " + longitude;
	}
}
