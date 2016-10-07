package common;

public class AutopilotHelper {

	public static final String ALTITUDE_CONTROLLER = "altitude";
	public static final String HEADING_CONTROLLER = "heading";
	public static final String PITCH_CONTROLLER = "pitch";
	public static final String ROLL_CONTROLER = "roll";

	/**
	 * Calculates the distance between current location and target location.
	 * 
	 * @param lat1
	 *            Latitude of current location
	 * @param long1
	 *            Longitude of current location
	 * @param lat2
	 *            Latitude of target location
	 * @param long2
	 *            Longitude of target location
	 * @return Returns the distance between the current and the target location
	 *         in meters.
	 */
	// a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
	// c = 2 ⋅ atan2( √a, √(1−a) )
	// d = R ⋅ c
	public static float getDistanceToWaypoint(double lat1, double long1, double lat2, double long2) {
		double EarthRadius = 6371000; // meters
		double phi1 = Math.toRadians(lat1);
		double phi2 = Math.toRadians(lat2);
		double deltaPhi = Math.toRadians(lat2 - lat1);
		double deltaLambda = Math.toRadians(long2 - long1);

		double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
				+ Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double distance = EarthRadius * c;

		return (float) distance;
	}

	/**
	 * Calculates the (true) heading from a given point of view to a target
	 * location.
	 * 
	 * @param lat1
	 *            Latitude of current location
	 * @param long1
	 *            Longitude of current location
	 * @param lat2
	 *            Latitude of target location
	 * @param long2
	 *            Longitude of target location
	 * @return Returns the (true) heading from a given point of view to a target
	 *         location.
	 */
	// θ = atan2(sin Δλ ⋅ cos φ2 , cos φ1 ⋅ sin φ2 − sin φ1 ⋅ cos φ2 ⋅ cos Δλ)
	// where φ1,λ1 is the start point, φ2,λ2 the end point (Δλ is the difference
	// in longtitude)
	public static float getTargetDestination(double lat1, double long1, double lat2, double long2) {
		double phi1 = Math.toRadians(lat1);
		double phi2 = Math.toRadians(lat2);
		double deltaLambda = Math.toRadians(long2 - long1);

		double y = Math.sin(deltaLambda) * Math.cos(phi2);
		double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);

		double bearing = Math.atan2(y, x);
		bearing = Math.toDegrees(bearing);

		return (float) bearing;
	}

	public static PIDParameters getAltitudePidParameters() {
		PIDParameters result = new PIDParameters(0.003, // "kp" 0,005
				0.000005, // "ki" 0,00005
				0.00015, // "kd" 0,00015

				0.0, // "min_output"
				1.0, // "max_output"
				10.0, // "max_integral" 
				10, // "controller_frequency"
				7 // "dummy_param"
		);
		return result;
	}

	public static PIDParameters getPitchPidParameters() {
		PIDParameters result = new PIDParameters(0.000075, // "kp"
				0.000001, // "ki"
				0.000000000009, // "kd" 0.000000000008
				-1.0, // "min_output"
				1.0, // "max_output"
				50.0, // "max_integral" 
				10, // "controller_frequency"
				7 // "dummy_param"
		);
		return result;
	}

	public static PIDParameters getRollPidParameters() {
		PIDParameters result = new PIDParameters(0.000075, // "kp"
				0.000001, // "ki"
				0.000000000009, // "kd" 0.000000000008

				-1.0, // "min_output"
				1.0, // "max_output"
				50.0, // "max_integral" 
				10, // "controller_frequency"
				7 // "dummy_param"
		);

		return result;
	}

	public static PIDParameters getHeadingPidParameters() {
		PIDParameters result = new PIDParameters(0.00003, // "kp" 0.000001
				0.0, // "ki" 0,0 
				0.0005, // "kd" 0.0005

				-1.0, // "min_output"
				1.0, // "max_output"
				100.0, // "max_integral" 
				10, // "controller_frequency"
				7 // "dummy_param"			
		);
		return result;
	}

	public static PIDParameters getPositionPidParameters() {
		PIDParameters result = new PIDParameters(0.095, // "kp"
				0.001, // "ki"
				9.0, // "kd"

				-10.0, // "min_output"
				10.0, // "max_output"
				100.0, // "max_integral" 
				10, // "controller_frequency"
				7 // "dummy_param"			
		);
		return result;
	}
}
