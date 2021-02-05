/**
 * A class representing the ElevatorMotor Object in the Elevator subsystem
 * @author Ammar Tosun
 */
public class ElevatorMotor {
	
	private boolean isRunning;
	
	public ElevatorMotor() {
		isRunning = false;
	}
	
	/**
	 * Setter for isRunning
	 * @param b - boolean to set it to
	 */
	public void setIsRunning(boolean b){
		System.out.println("ELEVATORMOTOR: Set isRunning motor: " + b);
		isRunning = b;
	}
	
	/**
	 * Getter for isRunning
	 * @return isRunning
	 */
	public boolean getIsRunning() {
		return isRunning;
	}
	
}