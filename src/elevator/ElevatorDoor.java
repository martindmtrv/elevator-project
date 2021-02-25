package elevator;

import event.Event;

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
		if (b)
			System.out.println("["+ Event.getRequestTime()+"]\t\tELEVATORDOOR: is opening");
		else
			System.out.println("["+Event.getRequestTime()+"]\t\tELEVATORDOOR: is closing");
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