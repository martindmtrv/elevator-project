/**
 * Class representing a single elevator. Will receive inputs from the ElevatorSubsystem to toggle its lights, buttons, motor and door
 * on arrival of floor and on input entered
 * @author Ammar Tosun
 */
public class Elevator implements Runnable {
	
	//static final String UP = "Moving Up", DOWN = "Moving Down", STILL = "Stopped";
	
	private ElevatorButton[] eButton;
	private ElevatorLamp[] eLamp;
	private ElevatorMotor eMotor;
	private ElevatorDoor eDoor;
	
	private int eID;
	private int currFloor;
	private int maxFloor;
	private DirectionType status;	//UP, DOWN, STILL/stopped
	
	/**
	 * Create a new Elevator 
	 * @param n - number of floors
	 * @param s - elevator name/ID
	 * @param c - currFloor
	 */
	public Elevator(int s, int n, int c) {
		eButton = new ElevatorButton[n];
		eLamp = new ElevatorLamp[n];
		eMotor = new ElevatorMotor();
		eDoor = new ElevatorDoor();
		eID = s;
		maxFloor = n;
		currFloor = c;
		status = DirectionType.STILL;
		
		// create all the elevator buttons and lamps
		for (int i = 0; i < n; ++i) {
			eButton[i] = new ElevatorButton(i+1);
			eLamp[i] = new ElevatorLamp(i+1);
		}
	}
	
	
	//Getter for current  floor of the elevator (currFloor)
	public int getCurrFloor() { return currFloor; }
	public int getID() { return eID; }
	public DirectionType getDirection() { return status; }
	
	//Go to the destination floor 
	//@param d - destination floor number
	public boolean visitFloor(int d) {
		//if destination is at an upper floor, go up until you reach it
		if (this.currFloor < d) {
			while (this.currFloor != d) {
				this.getStatus();
				this.moveUp();
			}
			this.arrived(d);
			this.getStatus();
			return true;
		}
		else if (this.currFloor > d) {
			while (this.currFloor != d) {
				this.getStatus();
				this.moveDown();
			}
			this.arrived(d);
			this.getStatus();
			return true;
		}
		else {
			this.arrived(d);
		}
		return false;
	}
	
	public void getStatus() {
		System.out.println(eID + " is moving " + status + " and is in this floor: " + currFloor);
	}
	
	public void moveUp() {
		if (currFloor < maxFloor) {
			status = DirectionType.UP;
			++currFloor;
			this.runMotor(true);
		}
		else {
			status = DirectionType.STILL;
			this.runMotor(false);
		}
		
	}
	public void moveDown() {
		if (currFloor > 0) {
			status = DirectionType.DOWN;
			--currFloor;
			this.runMotor(true);
		}
		else {
			status = DirectionType.STILL;
			this.runMotor(false);
		}
	}
	
	//Opening and closing the elevator door
	public void openDoor() { eDoor.setIsOpen(true); }
	public void closeDoor() { eDoor.setIsOpen(false); }
	
	//Running and stopping the elevator motor
	public void runMotor(boolean b) { eMotor.setIsRunning(b); }
	
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
	public void arrived(int n) {
		//maybe can un-lit lamp, only when the elevator door is open and motor has stopped??
		eButton[n].setIsPressed(false);
		eLamp[n].setIsLit(false);
	}	
	
}