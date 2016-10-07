package common;

/**
 * A PID controller with optional output bounds, output moving average filter
 * and integral error clamping.
 * 
 * Instances should be created through the Builder pattern using the
 * {@link #withGains(double, double, double)} method or by passing a set of
 * parameters to the {@link #PIDController(PIDParameters)} constructor.
 * 
 * @see PIDParameters
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class PIDController {
	private double kp; // Proportional gain
	private double ki; // Integral gain
	private double kd; // Differential gain

	private double alpha = 1.0f; // Low-pass filter ratio

	private Double minOutput = null;
	private Double maxOutput = null;
	private Double setpoint = null;
	private double maxIntegral = Double.MAX_VALUE;
	private double integral = 0.0f;
	private double lastError;
	private double lastOutput = Double.NaN;

	/** Default constructor inaccessible - use Builder methods */
	private PIDController() {
	}

	/**
	 * Construct a new PID controller with the supplied set of parameters.
	 *
	 * @param params
	 */
	public PIDController(PIDParameters params) {
		this.kp = params.getKp();
		this.kd = params.getKd();
		this.ki = params.getKi();

		this.minOutput = params.getMinOutput();
		this.maxOutput = params.getMaxOutput();
		this.alpha = params.getAlpha();
	}

	public PIDController(PIDParameters params, float integralInit) {
		this.kp = params.getKp();
		this.kd = params.getKd();
		this.ki = params.getKi();

		this.minOutput = params.getMinOutput();
		this.maxOutput = params.getMaxOutput();
		this.alpha = params.getAlpha();

		this.integral = integralInit;
	}

	/** Change the output value by applying the moving average filter */
	private double filter(double output) {
		if (Double.isNaN(lastOutput)) {
			lastOutput = output;
		}
		final double filtered = alpha * output + (1.0 - alpha) * lastOutput;
		lastOutput = filtered;
		return filtered;
	}

	/**
	 * Feed the current input to the controller and obtain updated output.
	 * 
	 * @param current
	 *            the current input value
	 * @return the controller output after applying optional bounds clamping and
	 *         output filtering
	 * @throws IllegalStateException
	 *             if the controller does not have a setpoint
	 */
	public float update(double current) {
		if (!hasSetpoint()) {
			throw new IllegalStateException("PID controller must have a setpoint");
		}

		final double error = setpoint - current;
		integral += error;

		// Clamp integral error to preset bounds to prevent integral windup
		if (integral > maxIntegral) {
			integral = maxIntegral;
		} else if (integral < -maxIntegral) {
			integral = -maxIntegral;
		}

		final double deltaErr = error - lastError;
		lastError = error;

		double output = kp * error + ki * integral + kd * deltaErr;
		output = filter(output);

		if (minOutput != null && output < minOutput) {
			output = minOutput;
		}

		if (maxOutput != null && output > maxOutput) {
			output = maxOutput;
		}
		return (float) output;
	}

	/**
	 * @return the output moving average filter coefficient
	 */
	public double getFilterRatio() {
		return alpha;
	}

	/**
	 * @return the optional lower output bound
	 */
	public Double getMinOutput() {
		return minOutput;
	}

	/**
	 * @return the optional upper output bound
	 */
	public Double getMaxOutput() {
		return maxOutput;
	}

	/**
	 * Check if this controller has a setpoint
	 * 
	 * @return the setpoint state
	 */
	public boolean hasSetpoint() {
		return setpoint != null;
	}

	/**
	 * @return the setpoint value
	 */
	public Double getSetpoint() {
		return setpoint;
	}

	/**
	 * Update the controller setpoint value
	 * 
	 * @param setpoint
	 *            the new setpoint
	 * @param reset
	 *            indicates if the integral error should be reset to 0
	 */
	public void setSetpoint(double setpoint, boolean reset) {
		if (reset) {
			integral = 0.0;
		}
		this.setpoint = setpoint;
	}

	/**
	 * Update the controller setpoint value
	 * 
	 * Note, that this operation resets the integral error to 0
	 * 
	 * @param setpoint
	 *            the new setpoint
	 */
	public void setSetpoint(double setpoint) {
		setSetpoint(setpoint, true);
	}

	/**
	 * @return the current integral error
	 */
	public double getIntegral() {
		return integral;
	}
}
