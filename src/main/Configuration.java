package main;
/**
 * Configuration class for the system
 * @author Martin Dimitrov
 */
public abstract class Configuration {
	// elevator system parameters
	public static final int NUM_FLOORS = 10;
	public static final int NUM_CARS = 1;
	public static final int INIT_CAR_FLOOR = 1;		//initial floor of the elevator when it's created

	// kinematics of the elevator (for position calculations)
	public static final double VELOCITY = 0;
	public static final double ACCELERATION = 0;

}