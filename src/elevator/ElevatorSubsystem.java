package elevator;
import java.util.Arrays;

import event.*;
import main.Configuration;
import scheduler.BoundedBuffer;

/**
 * Elevator Subsystem for the elevator project. Controls all the elevators and handles sending input to the scheduler
 * @author Ammar Tosun
 */
public class ElevatorSubsystem implements Runnable {
	
	private Elevator[] elevators;				
	private BoundedBuffer elevatorEvents;
	private BoundedBuffer schedulerEvents;
	private ElevatorState currState;
	private ElevatorState prevState;

	private Box box;
	
	/**
	 * Create a new ElevatorSubsystem 
	 * @param n - number of elevators/cars
	 * @param f - number of floors
	 * @param c - initial floor of the elevators
	 * @param elevatorQueue - events queue to be handled by the elevator subsystem 
	 * @param schedulerQueue - events to be read that come from the scheduler subsystem
	 */
	public ElevatorSubsystem(int n, int f, int c, BoundedBuffer elevatorQueue, BoundedBuffer schedulerQueue, Box box) {
		elevators = new Elevator[n];
		elevatorEvents = elevatorQueue;
		schedulerEvents = schedulerQueue;
		this.currState = ElevatorState.STILL; //init state

		this.box = box;

		// create elevators
		for (int x = 0; x < n; x++) {
			elevators[x] = new Elevator(x, f, c, this.box, this.schedulerEvents);
		}
	}
	
	/**
	 * The elevator subsystem will receive inputs from scheduler
	 * For iteration 1 it will be read from a file
	 */
	@Override
	public void run() {		
		Event reply, event;
		
		ElevatorButtonPressEvent ebEvent;
		FloorButtonPressEvent fbEvent;
		ElevatorMoveEvent emEvent;
		ElevatorCallToMoveEvent ectmEvent;
		ElevatorTripUpdateEvent etuEvent;
		
		// run until stopped
		while(!Thread.interrupted()) {

			event = (Event) elevatorEvents.removeFirst();
			
			//when the elevator is on the floor that is called
			if (event.getType() == EventType.ELEVATOR_BUTTONS) {
				ebEvent = (ElevatorButtonPressEvent) event;
	
				//get the destinations
				Integer[] destinations = ebEvent.getButtons();
				
				//loop through destinations and add a MoveElevatorEvent to the  elevatorEvents
				for (int f: destinations) {
					reply = new ElevatorMoveEvent(ebEvent.getCar(), elevators[ebEvent.getCar()].getCurrFloor(), f, elevators[ebEvent.getCar()].getDirection());
					elevatorEvents.addLast(reply);
				}
			} 
			//elevator called from a floor
			else if (event.getType() == EventType.FLOOR_BUTTON) {
				fbEvent = (FloorButtonPressEvent) event;
				
				reply = new ElevatorMoveEvent(elevators[0].getID(),elevators[0].getCurrFloor(), fbEvent.getFloor(), elevators[0].getDirection());
				elevatorEvents.addLast(reply);

			}
			//elevator is moving to a destination and sending a notification to the scheduler that elevator arrived
			else if (event.getType() == EventType.ELEVATOR_MOVING) {
				emEvent = (ElevatorMoveEvent) event;
				
				//go to the floor
				boolean arrived = elevators[emEvent.getCar()].visitFloor(emEvent.getDestination());
				
				//when arrived, notify scheduler
				if (arrived) {
					reply = new ElevatorArriveEvent(emEvent.getCar(), emEvent.getDestination(), elevators[emEvent.getCar()].getDirection());
					schedulerEvents.addLast(reply);
				}
			//new logic with iteration 2 scheduler implementations
			}else if(event.getType() == EventType.ELEVATOR_CALLED){
				//Elevator shall move
				ectmEvent = (ElevatorCallToMoveEvent) event;
				//set direction of the car which the scheduler has assigned to move
				box.put(ectmEvent);
			}else if(event.getType() == EventType.ELEVATOR_ARRIVAL_SENSOR) {
				etuEvent = (ElevatorTripUpdateEvent) event;
				//Notify elevators of and ElevatorTripUpdate
				box.put(etuEvent);
			}
		}
	}
	
	/**
	 * Getter method to get the elevators in the subsystem
	 * @return Array of elevators
	 */
	public Elevator[] getElevators(){
		return elevators;
	}
	
	/**
	 * Set the new state for the elevator 
	 * @param elevState State the elevator is in
	 */
	public void setState(ElevatorState elevState) {
		this.prevState = currState; //previous elevator state (may not be useful / check later)
		this.currState = elevState; //new elevator state
	}
	
	public ElevatorState getState() {
		return currState;
	}
	
	public static void main(String[] args) {
		Box box = new Box();
		Thread e = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR ,new BoundedBuffer(), new BoundedBuffer(), box));
		e.start();
	}
}