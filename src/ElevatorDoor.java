/**
 * A class representing the ElevatorDoor Object in the Elevator subsystem
 * @author Ammar Tosun
 */
public class ElevatorDoor {
	
	private boolean isOpen;		/* State of the door open or not */ 
	
	
	/**
	 * Create a new ElevatorDoor
	 * The door is closed initially 
	 */
	public ElevatorDoor() {
		isOpen = false;
	}
	
	/**
	 * Setter for isOpen
	 * @param b - boolean to set it to
	 */
	public void setIsOpen(boolean b){
		System.out.println("ELEVATORDOOR: Set isOpen door: " + b);
		isOpen = b;
	}
	
	/**
	 * Getter for isOpen
	 * @return isOpen
	 */
	public boolean getIsOpen() {
		return isOpen;
	}
	
}
