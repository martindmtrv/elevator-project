/**
 * Configuration class for the system
 * @author Martin Dimitrov
 */
public abstract class Configuration {
	// elevator system parameters
	static final int NUM_FLOORS = 10;
	static final int NUM_CARS = 1;
	static final int INIT_CAR_FLOOR = 1;		//initial floor of the elevator when it's created

	// kinematics of the elevator (for position calculations)
	static final double VELOCITY = 0;
	static final double ACCELERATION = 0;

}