/**
 * A class representing the ElevatorButton Object in the Elevator subsystem
 * @author Ammar Tosun
 */
public class ElevatorButton {
	
	private int floorNum; 		/* Floor number of this button */
	private boolean isPressed;	/* State of the button pressed or not */
	
	/**
	 * Create a new ElevatorButton
	 * @param n - which floor number does this button signal
	 */
	ElevatorButton(int n) {
		floorNum = n;
		isPressed = false;
	}
	
	/**
	 * Setter for isPressed
	 * @param b - boolean to set the value
	 */
	public void setIsPressed(boolean b){
		System.out.println("ELEVATORBUTTON: Set pressed: " + b);
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