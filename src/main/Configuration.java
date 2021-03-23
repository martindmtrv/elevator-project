package main;

/**
 * Configuration class for the system
 * @author Martin Dimitrov
 */
public abstract class Configuration {
	
	// Flags for alternate operate modes
	public static boolean VERBOSE = false;
	
	// elevator system parameters
	public static final int NUM_FLOORS = 22;
	public static final int NUM_CARS = 4;
	public static final int INIT_CAR_FLOOR = 1;		//initial floor of the elevator when it's created

	// kinematics of the elevator (for position calculations)
	public static final double VELOCITY = 0;
	public static final double ACCELERATION = 0;

	//Time from floor to floor (Numbers from Iteration 0) in ms
	public static final long LOAD_TIME = 1000; //9.51323 actual but using 1s for testing
	public static final long TRAVEL_TIME_BETWEEN_FLOOR = 9500; //~9.5s
	
	// timeout delay for timeouts (too long to hear from elevator)
	public static final long TIMEOUT_DELAY = 2000;
	
	//Fault sleep times
	public static final long DOOR_FAULT = 1000; //1s for a DOOR FAULT
	public static final long ARRIVAL_FAULT = 10000; //10s for a ARRIVAL SENSOR FAULT
	public static final long MOTOR_FAULT = 10000; //10s for a MOTOR FAILURE FAULT
	// test file
	public static final String TEST_FILE = "TestComm.txt";
	
	// ports info
	public static final int ELEVATOR_PORT = 50;
	public static final int SCHEDULER_LISTEN_ELEVATOR_PORT = 51;
	public static final int FLOOR_PORT = 60;
	public static final int SCHEDULER_LISTEN_FLOOR_PORT = 61;
	
	// address info
	// TODO: configure this for multiple machines running leave as "" for localhost
	public static final boolean USE_LOCALHOST = true;
	
	public static final String ELEVATOR_SYSTEM_ADDRESS = "";
	public static final String FLOOR_SYSTEM_ADDRESS = "";
	public static final String SCHEDULER_SYSTEM_ADDRESS = "";
	
}