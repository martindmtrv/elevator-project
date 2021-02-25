package elevator;

import event.DirectionType;

/**
 * A class representing the ElevatorMotor Object in the Elevator subsystem
 * @author Ammar Tosun
 */
public class ElevatorMotor {
	
	private boolean isRunning;
	private DirectionType direction;
	
	public ElevatorMotor() {
		isRunning = false;
		direction = DirectionType.STILL;
	}
	
	/**
	 * Setter for isRunning
	 * @param b - boolean to set it to
	 */
	public void setIsRunning(boolean b, DirectionType d){
		if (b && d == DirectionType.UP) {
			System.out.println("\tELEVATORMOTOR: starts running to move UP");
			direction = DirectionType.UP;
		}
		else if (b && d == DirectionType.DOWN) {
			System.out.println("\tELEVATORMOTOR: starts running to move DOWN");
			direction = DirectionType.DOWN;
		}	
		else {
			System.out.println("\tELEVATORMOTOR: stops running");
			direction = DirectionType.STILL;
		}
			
		isRunning = b;
	}
	
	/**
	 * Getter for isRunning
	 * @return isRunning
	 */
	public boolean getIsRunning() {
		return isRunning;
	}
	
	/**
	 * Getter for isRunning
	 * @return isRunning
	 */
	public DirectionType getDirection() {
		return direction;
	}
	
}