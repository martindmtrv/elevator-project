package floor;
import event.DirectionType;

/**
 * A Class to represent the Floor Lamp in the floor subsystem
 * @author Martin Dimitrov
 */
public class FloorLamp {
	private boolean isLit; 		/* State of the lamp lit or not */
	private DirectionType direction; 	/* Direction lamp points (not really used right now) */
	
	/**
	 * Create a FloorLamp Object
	 * @param d - DirectionType this FloorLamp represents
	 */
	FloorLamp(DirectionType d) {
		direction = d;
		isLit = false;
	}
	
	/**
	 * Setter for isLit
	 * @param b - boolean to set it to
	 */
	public void setIsLit(boolean b){
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
