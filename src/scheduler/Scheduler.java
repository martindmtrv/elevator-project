package scheduler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import event.*;
import elevator.ElevatorState;
import main.Configuration;

/**
 * Scheduler thread that handles the communication between the elevator thread and the floor thread.
 * Currently, it passes requests from the elevator thread to the floor thread and vice versa on a first-in,
 * first-out basis. The scheduler's logic will be upgraded in upcoming iterations.
 * @author Erdem Yanikomeroglu (itr 1), Martin Dimitrov (itr 2)
 */
public class Scheduler implements Runnable {
	public BoundedBuffer schedulerQueue;
	private BoundedBuffer elevatorQueue;
	private BoundedBuffer floorQueue;
	private State state;
	
	private ArrayList<ElevatorStatus> elevators;
	private ArrayList<FloorButtonPressEvent> unscheduled;
	
	/**
	 * Create a new scheduler object
	 * @param schedulerQueue
	 * @param elevatorQueue
	 * @param floorQueue
	 */
	public Scheduler(BoundedBuffer schedulerQueue, BoundedBuffer elevatorQueue, BoundedBuffer floorQueue) {
		this.schedulerQueue = schedulerQueue;
		this.elevatorQueue = elevatorQueue;
		this.floorQueue = floorQueue;
		
		unscheduled = new ArrayList<>();
		elevators = new ArrayList<>(Configuration.NUM_CARS);
		this.state = State.WAITING;
		
		// create all the arraylists for elevator destinations
		for (int i=0; i < Configuration.NUM_CARS; i++) {
			elevators.add(new ElevatorStatus(i));
		}
	}
	
	/**
	 * Return the first idle elevator
	 * @return e - idle elevator or null
	 */
	private ElevatorStatus findIdleElevator() {
		for (ElevatorStatus e: elevators) {
			if (e.getStatus() == ElevatorJobState.IDLE) {
				return e;
			}
		}
		return null;
	}
	/**
	 * Method to determine if an elevator can add another stop on the way,
	 * if it can, then add to that elevator
	 * @param d - direction the elevator should be going
	 * @param pickup - the floor number to pickup from
	 * @return id of the elevator (or -1 if none)
	 */
	private int elevatorEnRouteAdd(DirectionType d, int pickup) {
		HashSet<Integer> destinations;
		for (ElevatorStatus e: elevators) {
			destinations = e.getDestinations();
			if (e.getStatus() == ElevatorJobState.EN_ROUTE) {
				if (d == e.getDirection() && ((d == DirectionType.UP && pickup > e.getLocation()) || (d == DirectionType.DOWN && pickup < e.getLocation()))) {
					System.out.println(String.format("SCHEDULER: found enroute elevator %d going %s adding new stop %d to its destinations", e.getId(), d, pickup));
					destinations.add(pickup);
					return e.getId();
				} 
			}
		}
		return -1;
	}
	
	/**
	 * Logic for handling floor button presses, this method goes through the options for scheduling,
	 * updating scheduler elevator states, and passing the event appropriately to the elevator subsystem
	 * @param buttonEv - the event to handle
	 */
	private void handleFloorButtonPressEvent(FloorButtonPressEvent buttonEv) {
		ElevatorStatus elevator;
		int assigned;
		ElevatorCallToMoveEvent elevatorRequest;
		DirectionType toMove;
		
		System.out.println("SCHEDULER: Handling floor button press");
		
		// check if an elevator is on the way
		assigned = elevatorEnRouteAdd(buttonEv.getDirection(), buttonEv.getFloor());
		// if unsuccessful
		if (assigned == -1) {
			System.out.println("SCHEDULER: Unable to find elevator enroute to schedule");
			// assign to a free elevator
			elevator = findIdleElevator();
			
			// no idle elevators ... 
			if (elevator == null) {
				System.out.println("SCHEDULER: Placed in unassigned queue, going to wait for a free elevator");
				// leave in unassigned
				unscheduled.add(buttonEv);
			} else {
				System.out.println(String.format("SCHEDULER: elevator %d is IDLE sending request to move for pickup", elevator.getId()));
				
				// Handle case where the elevator is already here
				if (elevator.getLocation() == buttonEv.getFloor()) {
					elevator.setDirection(DirectionType.STILL);
					elevator.setStatus(ElevatorJobState.EN_ROUTE);
					elevator.setWorkingDirection(buttonEv.getDirection());
					
					floorQueue.addLast(new ElevatorArriveEvent(elevator.getId(), elevator.getLocation(), buttonEv.getDirection()));
				} else {
					elevator.getDestinations().add(buttonEv.getFloor());
					// set the proper direction
					if (buttonEv.getFloor() > elevator.getLocation()) {
						toMove = DirectionType.UP;
					} else  {
						toMove = DirectionType.DOWN;
					}
					// update elevator states
					elevator.setStatus(ElevatorJobState.PICKING_UP);
					elevator.setDirection(toMove);
					elevator.setWorkingDirection(buttonEv.getDirection());
					
					elevatorRequest = new ElevatorCallToMoveEvent(elevator.getId(), toMove);
					elevatorQueue.addLast(elevatorRequest);
				}
			}
		}
	}
	
	/**
	 * Logic for handling elevatorbutton presses, this method goes through the options for scheduling,
	 * updating scheduler elevator states, and passing the event appropriately to the elevator subsystem
	 * @param elevatorBEv - the event to handle
	 */
	private void handleElevatorButtonPressEvent(ElevatorButtonPressEvent elevatorBEv) {
		ElevatorStatus elevator;
		ElevatorCallToMoveEvent elevatorRequest;
		Object[] unscheduledEvents;
		
		// update elevator state
		elevator = elevators.get(elevatorBEv.getCar());
		
		
		elevator.getDestinations().addAll(Arrays.asList(elevatorBEv.getButtons())); //add all new elevator destinations
		elevator.setDirection(elevatorBEv.getDirection());
		
		if (elevator.getDestinations().size() > 0) {
			// update states
			elevator.setStatus(ElevatorJobState.EN_ROUTE);
			elevator.setWorkingDirection(elevatorBEv.getDirection());
			elevator.setDirection(elevatorBEv.getDirection());
			
			// get the elevator moving
			elevatorRequest = new ElevatorCallToMoveEvent(elevatorBEv.getCar(), elevator.getWorkingDirection());
			System.out.println("SCHEDULER: Sending event " + elevatorRequest + " to elevator");
			elevatorQueue.addLast(elevatorRequest);
		} else {
			elevator.setStatus(ElevatorJobState.IDLE);
			elevator.setDirection(DirectionType.STILL);
			elevator.setWorkingDirection(DirectionType.STILL);
			// since an elevator is now free, try and schedule the unassigned events
			
			// call event handle with all unassigned to try and get them in
			unscheduledEvents = unscheduled.toArray();
			unscheduled.clear();
			
			for (Object e: unscheduledEvents) {
				handleEvent((Event) e);
			}
		}
	}
	
	/**
	 * Method to handle the elevator arrival sensor updates
	 * @param easEvent - event to handle
	 */
	private void handleElevatorApproachSensorEvent(ElevatorApproachSensorEvent easEvent) {
		ElevatorStatus elevator = elevators.get(easEvent.getCar());
		ElevatorTripUpdateEvent etuEvent;
		int floorToReach;
		if (easEvent.getDirection() == DirectionType.DOWN) {
			floorToReach = elevator.getLocation() - 1;
		} else {
			floorToReach = elevator.getLocation() + 1;
		}
		
		// update the state
		elevator.setLocation(floorToReach);
		
		// check if this floor in the destinations OR is one of the limits (avoid crashing out)
		if (floorToReach == 1 || floorToReach == Configuration.NUM_FLOORS || elevator.getDestinations().contains(floorToReach)) {
			etuEvent = new ElevatorTripUpdateEvent(elevator.getId(), ElevatorTripUpdate.STOP);
		} else {
			etuEvent = new ElevatorTripUpdateEvent(elevator.getId(), ElevatorTripUpdate.CONTINUE);
		}
		
		elevatorQueue.addLast(etuEvent); //add new ElevatorTripUpdateEvent to elevator's queue
	}
	
	/**
	 * Handle event when elevator arrives
	 * @param eaEvent - event to handle
	 */
	private void handleElevatorArriveEvent(ElevatorArriveEvent eaEvent) {
		System.out.println("SCHEDULER: Sending event " + eaEvent + " to floor");
		ElevatorStatus elevator = elevators.get(eaEvent.getCar());
		
		elevator.setDirection(DirectionType.STILL);
		
		// remove from destinations
		elevator.getDestinations().remove(Integer.valueOf(eaEvent.getFloor()));
		
		// set proper working direction
		ElevatorArriveEvent reply = new ElevatorArriveEvent(eaEvent.getCar(), eaEvent.getFloor(), elevator.getWorkingDirection());
		
		floorQueue.addLast(reply);
	}
	
	/**
	 * General method to handle events on the scheduler, breaks off into specific event types
	 * @param event - generic event pulled from scheduler bounded buffer queue
	 */
	private void handleEvent(Event event) {
		switch(event.getType()) {
			case FLOOR_BUTTON: {
				handleFloorButtonPressEvent((FloorButtonPressEvent) event);
				break;
			}
			case ELEVATOR_BUTTONS: {
				handleElevatorButtonPressEvent((ElevatorButtonPressEvent) event);
				break;
			}
			case ELEVATOR_ARRIVED:
				handleElevatorArriveEvent((ElevatorArriveEvent) event);
				break;
			case ELEVATOR_APPROACH_SENSOR: //Event sent from elevator informing of approach of a arrival sensor
				handleElevatorApproachSensorEvent((ElevatorApproachSensorEvent) event);
				break;
			default:
				System.out.println("SCHEDULER: Unhandled event " + event);
		}
	}
	
	/**
	 * Set the new state for the elevator 
	 * @param newState State the elevator is in
	 */
	public void setState(State newState) {
		this.state = newState;
	}
	/**
	 * Getter method to get the current state of Scheduler.
	 * i.e: WAITING, RECEIVING, SENDING or INVALID
	 * @return The schedulers current state
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Return a string of current state
	 * @return String representing the current State
	 */
	public String printState(){
		String s = "";
		switch(state) {
			case RECEIVING:
				s ="Receiving State";
				break;
			case WAITING:
				s= "Waiting State";
				break;
			case SENDING:
				s = "Sending State";
				break;
			case INVALID:
				s= "Invalid State";
				break;
		}
		return s;
	}
	
	@Override
	public void run() {
		Event event;

		// handle events forever
		while(!Thread.interrupted()) {
			event = (Event)schedulerQueue.removeFirst();
			handleEvent(event);
		}
	}
	
}
