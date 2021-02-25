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

	//Time from floor to floor (Numbers from Iteration 0) in ms
	public static final long LOAD_TIME = 1000; //9.51323 actual but using 1s for testing
	public static final long TRAVEL_TIME_BETWEEN_FLOOR = 9500; //~9.5s

}