package elevator;
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
		if (b)
			System.out.println("\tELEVATORMOTOR: starts running");
		else
			System.out.println("\tELEVATORMOTOR: stops running");
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