package elevator;

import event.Event;

/**
 * A Class to represent the Elevator Lamp in the elevator subsystem
 * @author Ammar Tosun
 */
public class ElevatorLamp {
	
	private boolean isLit; 		/* State of the lamp lit or not */
	private int floorNum; 		/* Floor number that it's showing (not really used right now) */
	
	/**
	 * Create a ElevatorLamp Object
	 * @param n - floor number this ElevatorLamp represents
	 */
	ElevatorLamp(int n) {
		floorNum = n;
		isLit = false;
	}
	
	/**
	 * Setter for isLit
	 * @param b - boolean to set it to
	 */
	public void setIsLit(boolean b){
		System.out.println("["+ Event.getCurrentTime()+"]\t\tELEVATORLAMP: lamp " + this.floorNum + " is lit: " + b);
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