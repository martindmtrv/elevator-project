package elevator;
import event.*;
import main.Configuration;
import scheduler.BoundedBuffer;
import scheduler.ElevatorTripUpdate;

/**
 * Class representing a single elevator. Will receive inputs from the ElevatorSubsystem to toggle its lights, buttons, motor and door
 * on arrival of floor and on input entered
 * @author Ammar Tosun (I1) & Alex Cameron (I2)
 */
public class Elevator implements Runnable{
	
	private ElevatorButton[] eButton;
	private ElevatorLamp[] eLamp;
	private ElevatorMotor eMotor;
	private ElevatorDoor eDoor;
	
	private int eID;
	private int currFloor;
	private int maxFloor;
	private ElevatorState state;		
	private DirectionType direction; 	//UP, DOWN, STILL

	private final Box box;
	private BoundedBuffer elevatorEvents;
	
	/**
	 * Create a new Elevator 
	 * @param n - number of floors
	 * @param s - elevator name/ID
	 * @param c - currFloor
	 */
	public Elevator(int s, int n, int c, Box box, BoundedBuffer elevatorQueue) {
		eButton = new ElevatorButton[n+1];
		eLamp = new ElevatorLamp[n+1];
		eMotor = new ElevatorMotor();
		eDoor = new ElevatorDoor();
		eID = s;
		maxFloor = n;
		currFloor = c;
		direction = DirectionType.STILL;
		state = ElevatorState.IDLE;
		this.box = box;
		this.elevatorEvents = elevatorQueue;
		
		if(Configuration.VERBOSE) {
			System.out.println("\t\tELEVATOR: Car " + eID + " Initialized to IDLE");
		}

		// create all the elevator buttons and lamps
		for (int i = 1; i < n+1; ++i) {
			eButton[i] = new ElevatorButton(i);
			eLamp[i] = new ElevatorLamp(i);
		}
	}

	public ElevatorState getState() { return state;}
	public DirectionType getDirection() { return direction;}
	public int getNumFloors() {return maxFloor;}
	public int getCurrFloor() {return currFloor;}
	

	/**
	 * Press an elevator button to go to that floor and lit that elevator lamp
	 * @param n - number of the floor/button pressed to request to go
	 */
	public void pressButton(int n) {
		//maybe can press button, only when the elevator door is closed??
		eButton[n].setIsPressed(true);
		eLamp[n].setIsLit(true);
	}
	public void getStatus() {
		if (this.state != ElevatorState.IDLE)
			System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " is " + state + ", and is approaching floor: " + currFloor);
		else
			System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " is " + state + ", and currently on floor: " + currFloor);
	}
	
	//Running and stopping the elevator motor
	public void runMotor(boolean b, ElevatorState d) { eMotor.setIsRunning(b, d); }

	/**
	 * When the elevator arrives to the floor, the elevator lamp and button should not lit
	 * @param n - number of the floor the elevator has arrived
	 */
	public void arrived(int n) {
		//upon arrival: stop motor, open door, un-lit lamp, un-press button
		System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " has arrived floor " + n);
		
		if(Configuration.VERBOSE) {
			System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " " + this.state + "->IDLE");
		}
		
		state = ElevatorState.IDLE;
		direction = DirectionType.STILL;
		this.runMotor(false, state);
		this.eDoor.setIsOpen(true);
		eLamp[n].setIsLit(false);
		eButton[n].setIsPressed(false);
		
	}

	/**
	 * Handles the event where the scheduler chooses a particular elevator to begin moving.
	 * @param ectmEvent ElevatorCallToMoveEvent which calls elevator to move
	 */
	public void handleElevatorCalledEvent(ElevatorCallToMoveEvent ectmEvent){
		if(eID == ectmEvent.getCar()){ //check if car is the desired car
			getStatus();
			for(Integer i : ectmEvent.getDestinationToLight()){ //check all new destinations
				pressButton(i); //press elevator buttons when destinations are added to elevator trip
			}
			if(eDoor.getIsOpen()){ //close doors if they are open
				eDoor.setIsOpen(false);
			}
			if(ectmEvent.getDirection() == DirectionType.UP){
				//direction => go up & turn on motor
				if(Configuration.VERBOSE) {
					System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " " + this.state + "->MOVING_UP");
				}
				state = ElevatorState.MOVING_UP;
				direction = DirectionType.UP;
				runMotor(true, state);
			}else{
				//direction => go down & turn on motor
				if(Configuration.VERBOSE) {
					System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " " + this.state + "->MOVING_DOWN");
				}
				state = ElevatorState.MOVING_DOWN;
				direction = DirectionType.DOWN;
				runMotor(true, state);
			}
			
			try {
				System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: CAR "+ eID +": Sleep " +Configuration.TRAVEL_TIME_BETWEEN_FLOOR/2000+"s");
				Thread.sleep((Configuration.TRAVEL_TIME_BETWEEN_FLOOR/2)); //sleep 5s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ElevatorApproachSensorEvent easEvent = new ElevatorApproachSensorEvent(eID,direction);
			elevatorEvents.addLast(easEvent); //notify scheduler that arrival sensor triggered
		}else{ //notify other elevators
			box.notifyAll();
		}
	}

	/**
	 * Handles the arrival sensor trip updates received from the scheduler every time an elevator reaches
	 * half way points between floors while traveling.
	 * @param etuEvent ElevatorTripUpdateEvent Notifies the elevator to continue or stop before reaching next floor
	 */
	public void handleElevatorTripUpdateEvent(ElevatorTripUpdateEvent etuEvent){
		if(eID == etuEvent.getCar()) { //car IDs are same
			currFloor = etuEvent.getApproachingFloor(); //update curr floor
			getStatus();
			if (etuEvent.getUpdate() == ElevatorTripUpdate.STOP) { //Scheduler tells elevator to stop at next floor
				try {
					System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: CAR "+ eID +": Sleep " +Configuration.TRAVEL_TIME_BETWEEN_FLOOR/2000+ "s Arrival Sensor: STOP");
					Thread.sleep(Configuration.TRAVEL_TIME_BETWEEN_FLOOR/2); //sleep 5s and stop
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				arrived(etuEvent.getApproachingFloor()); //arrive at next floor
				ElevatorArriveEvent eaEvent = new ElevatorArriveEvent(eID,etuEvent.getApproachingFloor(), direction);
				elevatorEvents.addLast(eaEvent);
				//Elevator must now wait for passengers to get on at the arrived floor before departing
				try {
					if(Configuration.VERBOSE) {
						System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " " + this.state + "->LOADING_PASSENGER");
					}
					state = ElevatorState.LOADING_PASSENGER;
					System.out.println("["+Event.getCurrentTime()+"]\tCAR "+ eID +": Sleeping " + Configuration.LOAD_TIME/1000 + "s for passengers to load.");
					Thread.sleep(Configuration.LOAD_TIME);
					if(Configuration.VERBOSE) {
						System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR: Car " + eID + " " + this.state + "->MOVING_UP");
					}
					state = ElevatorState.IDLE;
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			} else {
				try {
					System.out.println("["+Event.getCurrentTime()+"]\tELEVATOR CAR "+ eID +": Sleep "+Configuration.TRAVEL_TIME_BETWEEN_FLOOR/1000+ " Arrival Sensor: CONTINUE");
					Thread.sleep(Configuration.TRAVEL_TIME_BETWEEN_FLOOR); //sleep ~10s and send another Elevator arrival sensor event
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				ElevatorApproachSensorEvent easEvent = new ElevatorApproachSensorEvent(eID, direction);
				elevatorEvents.addLast(easEvent); //notify scheduler that arrival sensor triggered
			}
		}else{
			box.notifyAll();
		}
	}

	@Override
	public void run(){
		while(true){
			Event event = box.get();
			switch (event.getType()) {
				case ELEVATOR_CALLED: //Elevator called to move
					handleElevatorCalledEvent((ElevatorCallToMoveEvent) event);
					break;
				
				case ELEVATOR_TRIP_UPDATE: //Arrival sensor
					handleElevatorTripUpdateEvent((ElevatorTripUpdateEvent) event);
					break;
			}
			
		}
	}
}