/**
 * A class representing the FloorButton Object in the Floor subsystem
 * @author Martin Dimitrov
 *
 */
public class FloorButton {
	// Constants for direction types
	static final String UP = "Up", DOWN = "Down";
	
	private String direction;
	private boolean isPressed;
	
	/**
	 * Create a new FloorButton
	 * @param d - which direction does this button signal FloorButton.UP or FloorButton.DOWN
	 */
	FloorButton(String d) {
		direction = d;
		isPressed = false;
	}
	
	/**
	 * Setter for isPressed
	 * @param b - boolean to set the value
	 */
	public void setIsPressed(boolean b){
		isPressed = b;
	}
	
	/**
	 * Press the button, sets isPressed true
	 * @return direction - the value which this floor button should send in the request
	 */
	public String press() {
		isPressed = true;
		return direction;
	}
}
