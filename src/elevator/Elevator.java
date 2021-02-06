package elevator;
import event.DirectionType;

/**
 * Class representing a single elevator. Will receive inputs from the ElevatorSubsystem to toggle its lights, buttons, motor and door
 * on arrival of floor and on input entered
 * @author Ammar Tosun
 */
public class Elevator {
	
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
		eButton = new ElevatorButton[n+1];
		eLamp = new ElevatorLamp[n+1];
		eMotor = new ElevatorMotor();
		eDoor = new ElevatorDoor();
		eID = s;
		maxFloor = n;
		currFloor = c;
		status = DirectionType.STILL;
		
		// create all the elevator buttons and lamps
		for (int i = 1; i < n+1; ++i) {
			eButton[i] = new ElevatorButton(i);
			eLamp[i] = new ElevatorLamp(i);
		}
	}
	
	
	//Getter for current  floor of the elevator (currFloor)
	public int getCurrFloor() { return currFloor; }
	public int getID() { return eID; }
	public DirectionType getDirection() { return status; }
	
	
	//Go to the destination floor 
	//@param d - destination floor number
	public boolean visitFloor(int d) {
		this.getStatus();
		System.out.println("\tCar " + eID + " is in floor " + currFloor + " and will go to floor: " + d);
		
		//if elevator door is not closed yet, close elevator door
		if (eDoor.getIsOpen() ==  true)
			this.eDoor.setIsOpen(false);

		//if elevator button not pressed, press the button with the destination floor number
		if (eButton[d].getIsPressed() != true)
			this.eButton[d].setIsPressed(true);
		
		//if elevator lamp on for that floor not lit yet, lit the eLamp for that floor number button
		if (eLamp[d].getIsLit() != true)
			this.eLamp[d].setIsLit(true);
		
		//if destination is at an upper floor, go up until you reach it
		if (this.currFloor < d) {
			while (this.currFloor != d) {
				this.moveUp();
			}
			this.arrived(d);
			this.getStatus();
			return true;
		}
		else if (this.currFloor > d) {
			while (this.currFloor != d) {
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
		if (this.status != DirectionType.STILL)
			System.out.println("ELEVATOR: Car " + eID + " moving " + status + ", is currently on floor: " + currFloor);
		else
			System.out.println("ELEVATOR: Car " + eID + " is " + status + ", and currently on floor: " + currFloor);
	}
	
	public void moveUp() {
		if (currFloor < maxFloor) {
			status = DirectionType.UP;
			++currFloor;
			if(this.eMotor.getIsRunning() != true)
				this.runMotor(true);
		}
		else {
			status = DirectionType.STILL;
			this.runMotor(false);
		}
		System.out.println("\tCar " + eID + (status==DirectionType.STILL ? " is " : " moving ") +  status + ", currently on floor: " + currFloor);
		
	}
	public void moveDown() {
		if (currFloor > 0) {
			status = DirectionType.DOWN;
			--currFloor;
			if(this.eMotor.getIsRunning() != true)
				this.runMotor(true);
		}
		else {
			status = DirectionType.STILL;
			this.runMotor(false);
		}
	}
	
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
		//upon arrival: stop motor, open door, un-lit lamp, un-press button
		System.out.println("ELEVATOR: Car " + eID + " has arrived floor " + n);
		
		this.runMotor(false);
		this.eDoor.setIsOpen(true);
		eLamp[n].setIsLit(false);
		eButton[n].setIsPressed(false);
		
	}
	
	/**
	 * Getter method to return number of floors
	 * @return maxFloor - number of floors
	 */
	public int getNumFloors(){
		return maxFloor;
	}
	
}