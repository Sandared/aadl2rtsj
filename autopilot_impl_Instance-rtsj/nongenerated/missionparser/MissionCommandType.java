package missionparser;

/**
 * 
 * @author Manuel Hoss (manuel.hoss@hotmail.de)
 */
public enum MissionCommandType {

								/**
								 * The Copter will fly a straight line to the specified latitude, longitude
								 * and altitude. It will then wait at the point for a specified delay time
								 * and then proceed to the next waypoint. MissionCommandString: Index 0 3 19
								 * Delay 0 0 Heading Lat Lon Alt 1
								 */
								MAV_CMD_NAV_LOITER_TIME,

								/**
								 * Fly to the specified location. If zero is specified for a
								 * latitude/longitude/altitude parameter then the current location value for
								 * the parameter will be used. MissionCommandString: Index 0 3 16 0 0 0 0
								 * Lat Lon Alt 1
								 */
								MAV_CMD_NAV_WAYPOINT,

								/**
								 * Fly to the specified location. If zero is specified for a
								 * latitude/longitude/altitude parameter then the current location value for
								 * the parameter will be used. If skip range is reached, the next waypoint
								 * is chosen. MissionCommandString: Index 0 3 16 0 SkipRange 0 0 Lat Lon Alt
								 * 1
								 */
								MAV_CMD_NAV_SKIP
}
