package elevator;

import event.DirectionType;
import event.Event;

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
	public void setIsRunning(boolean b, ElevatorState d){
		if (b && d == ElevatorState.MOVING_UP) {
			System.out.println("["+ Event.getCurrentTime()+"]\t\tELEVATORMOTOR: starts running to move UP");
			direction = DirectionType.UP;
		}
		else if (b && d == ElevatorState.MOVING_DOWN) {
			System.out.println("["+Event.getCurrentTime()+"]\t\tELEVATORMOTOR: starts running to move DOWN");
			direction = DirectionType.DOWN;
		}	
		else {
			System.out.println("["+Event.getCurrentTime()+"]\t\tELEVATORMOTOR: stops running");
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