/**
 * A Class to represent the Floor Lamp in the floor subsystem
 * @author Ammar Tosun
 */
public class ElevatorLamp {
	private boolean isLit; 		/* State of the lamp lit or not */
	private int floor; 	/* Direction lamp points (not really used right now) */
	
	/**
	 * Create a ElevatorLamp Object
	 * @param n - floor this ElevatorLamp represents the floor number that is requested to go
	 */
	ElevatorLamp(int n) {
		floor = n;
		isLit = false;
	}
	
	/**
	 * Setter for isLit
	 * @param b - boolean to set it to
	 */
	public void setIsLit(boolean b){
		System.out.println("ElevatorLamp: Set lamp: " + b);
		isLit = b;
	}
	
	/**
	 * Getter for isLit
	 * @return isLit
	 */
	public boolean getIsLit() {
		return isLit;
	}
}

