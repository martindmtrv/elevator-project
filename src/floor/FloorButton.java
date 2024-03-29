package floor;
import event.DirectionType;
import java.io.Serializable;

/**
 * A class representing the FloorButton Object in the Floor subsystem
 * @author Martin Dimitrov
 */
public class FloorButton implements Serializable {
	private static final long serialVersionUID = -2235218683007063023L;

	// Constants for direction types
	static final String UP = "Up", DOWN = "Down";
	
	private DirectionType direction; 	/* Direction of this button */
	private boolean isPressed;	/* State of the button pressed or not */
	
	/**
	 * Create a new FloorButton
	 * @param d - which direction does this button signal FloorButton.UP or FloorButton.DOWN
	 */
	FloorButton(DirectionType d) {
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
	 * Getter for isPressed
	 * @return isPressed
	 */
	public boolean getIsPressed() {
		return isPressed;
	}
}
