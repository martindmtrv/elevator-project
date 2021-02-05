/**
 * Class representing a single elevator. Will receive inputs from the ElevatorSubsystem to toggle its lights, buttons, motor and door
 * on arrival of floor and on input entered
 * @author Ammar Tosun
 */
public class Elevator implements Runnable{
	
	private ElevatorButton[] eButton;
	private ElevatorLamp[] eLamp;
	private ElevatorMotor eMotor;
	private ElevatorDoor eDoor;
	private String eName;
	
	/**
	 * Create a new Elevator 
	 * @param n - number of floors
	 */
	public Elevator(String s, int n) {
		eButton = new ElevatorButton[n];
		eLamp = new ElevatorLamp[n];
		eMotor = new ElevatorMotor();
		eDoor = new ElevatorDoor();
		eName = s;
		
		// create all the elevator buttons and lamps
		for (int i = 1; i <= n; ++i) {
			eButton[i] = new ElevatorButton(i);
			eLamp[i] = new ElevatorLamp(i);
		}
	}
	
	//Opening and closing the elevator door
	public void openDoor() { eDoor.setIsOpen(true); }
	public void closeDoor() { eDoor.setIsOpen(false); }
	
	//Running and stopping the elevator motor
	public void runMotor() { eMotor.setIsRunning(true); }
	public void stopMotor() { eMotor.setIsRunning(false); }
	
	/**
	 * Press an elevator button to go to that floor and lit that elevator lamp
	 * @param n - number of the floor/button pressed to request to go
	 */
	public void pressButton(int n) { 
		//maybe can press button, only when the elevator door is closed??
		eButton[n].setIsPressed(true);
		eLamp[n].setIsLit(true);	
	}
	
	/**
	 * When the elevator arrives to the floor, the elevator lamp and button should not lit
	 * @param n - number of the floor the elevator has arrived
	 */
	public void arrivedn(int n) {
		//maybe can un-lit lamp, only when the elevator door is open and motor has stopped??
		eButton[n].setIsPressed(false);
		eLamp[n].setIsLit(false);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}
