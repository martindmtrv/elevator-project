
/**
 * A Class to represent the Floor Lamp in the floor subsystem
 * @author Martin Dimitrov
 *
 */
public class FloorLamp {
	private boolean isLit;
	private String direction;
	
	/**
	 * Create a FloorLamp Object
	 * @param d - direction this FloorLamp represents FloorButton.UP or FloorButton.DOWN
	 */
	FloorLamp(String d) {
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
}
