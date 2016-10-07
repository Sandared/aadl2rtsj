package common;

import java.util.Scanner;

public class MyCommands extends Thread {

	private float pitch = 0.0f;
	private float roll = 0.0f;
	private float heading = 0.0f;
	private float altitude = 0.0f;
	private boolean loiter = false;

	private void init() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Enter altitude");
			altitude = scanner.nextFloat();

			System.out.print("Enter heading");
			heading = scanner.nextFloat();

			System.out.print("Enter pitch");
			pitch = scanner.nextFloat();

			System.out.print("Enter roll");
			roll = scanner.nextFloat();

			System.out.print("Enter loiter");
			loiter = scanner.nextBoolean();
		}
	}

	@Override
	public void run() {
		super.run();
		init();
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public float getHeading() {
		return heading;
	}

	public float getAltitude() {
		return altitude;
	}

	public boolean isLoiter() {
		return loiter;
	}

}
