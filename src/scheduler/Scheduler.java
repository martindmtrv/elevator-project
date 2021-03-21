package scheduler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import event.*;
import floor.Floor;
import main.Configuration;
import rpc.RpcHandler;

/**
 * Scheduler thread that handles the communication between the elevator thread and the floor thread.
 * Currently, it passes requests from the elevator thread to the floor thread and vice versa on a first-in,
 * first-out basis. The scheduler's logic will be upgraded in upcoming iterations.
 * @author Erdem Yanikomeroglu (itr 1, 2), Martin Dimitrov (itr 1, 2), Ammar Tosun (itr 3)
 */
public class Scheduler implements Runnable {
	public BoundedBuffer schedulerQueue;
	private BoundedBuffer elevatorQueue;
	private BoundedBuffer floorQueue;
	private State state;
	
	private Floor[] floors;
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
		
		//Added output for Unit testing purposes
		if(Configuration.VERBOSE) {
			System.out.println("\t\tSCHEDULER: Initialize state to WAITING");
		}
		
		// create all the arraylists for elevator destinations
		for (int i=0; i < Configuration.NUM_CARS; i++) {
			elevators.add(new ElevatorStatus(i));
		}
		
		floors = new Floor[Configuration.NUM_FLOORS];
		
		// create all the floors and buffers
		for (int x = 0; x < Configuration.NUM_FLOORS; x++) {
			floors[x] = new Floor(x+1);
		}
	}
	
	/**
	 * Business logic for the passenger pick up:
	 * Find the closest available elevator to pick up, 
	 * so that the waiting time for passengers at floors is minimized
	 * 
	 * @param d - direction the elevator should be going
	 * @param pickup - the floor number to pickup from
	 * @return closestElevator - closest elevator or null if no available
	 */ 
	private ElevatorStatus findClosestElevator(DirectionType dir, int pickup) {
		ElevatorStatus closestElevator = null;
		int min = Configuration.NUM_FLOORS;
		int diff = 0;
		for (ElevatorStatus e: elevators) {
			// elevator can't pick up if it's already on-route to picking up
			// or if its in a fault state (itr 4)
			if (e.getStatus() == ElevatorJobState.PICKING_UP || e.getStatus() == ElevatorJobState.FAULT)	
				continue;
			
			else if ((e.getStatus() == ElevatorJobState.IDLE) || 	// if the elevator is idle or
					(e.getDirection() == dir && (
						 (e.getDirection() == DirectionType.UP && e.getLocation() < pickup) || 
						 (e.getDirection() == DirectionType.DOWN && e.getLocation() > pickup)))
			){	
				diff = Math.abs(e.getLocation() - pickup);
				if (min > diff) {
					min = diff;
					closestElevator = e; 
				}
			}
		}
		
		return closestElevator;
	}
	
	/**
	 * Logic for handling floor button presses, this method goes through the options for scheduling,
	 * updating scheduler elevator states, and passing the event appropriately to the elevator subsystem
	 * @param buttonEv - the event to handle
	 */
	private void handleFloorButtonPressEvent(FloorButtonPressEvent buttonEv) {
		
		//Updating floor states
		if(!buttonEv.getSeen()) {
			floors[buttonEv.getFloor() - 1] = buttonEv.getState();
		}
		
		ElevatorStatus elevator;
		ElevatorCallToMoveEvent elevatorRequest;
		DirectionType toMove;
		
		// output some info
		System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: Handling floor button event pickup %d direction %s", buttonEv.getFloor(), buttonEv.getDirection()));
		
		
		elevator = findClosestElevator(buttonEv.getDirection(), buttonEv.getFloor());
		
		if (elevator == null) {		// no available elevator
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Placed in unassigned queue, going to wait for a free elevator");
			unscheduled.add(buttonEv);	
			return;
		}
		
		if (elevator.getStatus() == ElevatorJobState.EN_ROUTE ) {
			HashSet<Integer> destinations;
			destinations = elevator.getDestinations();
			System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: found enroute elevator %d going %s adding new stop %d to its destinations", elevator.getId(), buttonEv.getDirection(), buttonEv.getFloor()));
			destinations.add(buttonEv.getFloor());
			// successfully assigned to enroute elevator
			System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: Assigned pickup at %d %s to elevator %d ENROUTE", buttonEv.getFloor(), buttonEv.getDirection(), elevator.getId()));
			return;
		}
		
		if (elevator.getStatus() == ElevatorJobState.IDLE) {
			System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: elevator %d is IDLE sending request to move for pickup", elevator.getId()));
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
			return;
		}
	}
	
	/**
	 * Logic for handling elevatorbutton presses, this method goes through the options for scheduling,
	 * updating scheduler elevator states, and passing the event appropriately to the elevator subsystem
	 * @param elevatorBEv - the event to handle
	 */
	private void handleElevatorButtonPressEvent(ElevatorButtonPressEvent elevatorBEv) {
		
		//Updating floor states
		if(!elevatorBEv.getSeen()) {
			floors[elevatorBEv.getState().getFloorNum() - 1] = elevatorBEv.getState();
		}
		
		ElevatorStatus elevator;
		ElevatorCallToMoveEvent elevatorRequest;
		Object[] unscheduledEvents;
		
		// update elevator state
		elevator = elevators.get(elevatorBEv.getCar());
		
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Adding elevator button presses: " + Arrays.toString(elevatorBEv.getButtons()) + " to elevator " + elevatorBEv.getCar());
		
		elevator.getDestinations().addAll(Arrays.asList(elevatorBEv.getButtons())); //add all new elevator destinations
		//elevator.setDirection(elevatorBEv.getDirection());
		
		if (elevator.getDestinations().size() > 0) {
			// update states
			elevator.setStatus(ElevatorJobState.EN_ROUTE);
			elevator.setWorkingDirection(elevatorBEv.getDirection());
			elevator.setDirection(elevatorBEv.getDirection());
			
			// get the elevator moving
			elevatorRequest = new ElevatorCallToMoveEvent(elevatorBEv.getCar(), elevator.getWorkingDirection(), elevatorBEv.getButtons());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Sending event " + elevatorRequest + " to elevator");
			elevatorQueue.addLast(elevatorRequest);
		} else {
			elevator.setStatus(ElevatorJobState.IDLE);
			elevator.setDirection(DirectionType.STILL);
			elevator.setWorkingDirection(DirectionType.STILL);
			
			System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: Elevator %d is now IDLE", elevator.getId()));
			// since an elevator is now free, try and schedule the unassigned events
			
			// call event handle with all unassigned to try and get them in
			unscheduledEvents = unscheduled.toArray();
			unscheduled.clear();
			
			if (unscheduledEvents.length > 0) {
				System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: free elevator detected, trying to schedule " + unscheduledEvents.length + " unassigned events");
				
				for (Object e: unscheduledEvents) {
					handleEvent((Event) e);
				}
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
		
		System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: elevator %d is approaching floor %d going %s", easEvent.getCar(), floorToReach, elevator.getDirection()));
		
		// update the state
		elevator.setLocation(floorToReach);
		
		// check if this floor in the destinations OR is one of the limits (avoid crashing out)
		if (floorToReach == 1 || floorToReach == Configuration.NUM_FLOORS || elevator.getDestinations().contains(floorToReach)) {
			etuEvent = new ElevatorTripUpdateEvent(elevator.getId(), floorToReach, ElevatorTripUpdate.STOP);
		} else {
			etuEvent = new ElevatorTripUpdateEvent(elevator.getId(), floorToReach, ElevatorTripUpdate.CONTINUE);
		}
		
		System.out.println(String.format("["+Event.getCurrentTime()+"]\tSCHEDULER: sending trip update %s to elevator %d", etuEvent.getUpdate(), etuEvent.getCar()));
		
		
		elevatorQueue.addLast(etuEvent); //add new ElevatorTripUpdateEvent to elevator's queue
	}
	
	/**
	 * Handle event when elevator arrives
	 * @param eaEvent - event to handle
	 */
	private void handleElevatorArriveEvent(ElevatorArriveEvent eaEvent) {
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Sending event " + eaEvent + " to floor");
		ElevatorStatus elevator = elevators.get(eaEvent.getCar());
		
		elevator.setDirection(DirectionType.STILL);
		
		// remove from destinations
		elevator.getDestinations().remove(Integer.valueOf(eaEvent.getFloor()));
		
		if (elevator.getLocation() == 1) {
			elevator.setWorkingDirection(DirectionType.UP);
		} else if (elevator.getLocation() == Configuration.NUM_FLOORS) {
			elevator.setWorkingDirection(DirectionType.DOWN);
		}
		
		// set proper working direction
		ElevatorArriveEvent reply = new ElevatorArriveEvent(eaEvent.getCar(), eaEvent.getFloor(), elevator.getWorkingDirection());
		
		floorQueue.addLast(reply);
	}
	
	/**
	 * Handle fault events
	 * @param efuEvent - event to handle
	 */
	private void handleElevatorFaultEvent(Fault efEvent) {
		ElevatorStatus elevator = elevators.get(efEvent.getCar());
		elevator.setDirection(DirectionType.STILL);
		
		if (efEvent.getFaultType() == FaultType.DOOR_STUCK) {
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Fault detected: DOOR STUCK, DOOR STUCK " + efEvent);
			System.out.println("retrying to close door...");
			ElevatorArriveEvent retry = new ElevatorArriveEvent(efEvent.getCar(), 1, elevator.getWorkingDirection());
			floorQueue.addLast(retry);
		}
		else if (efEvent.getFaultType() == FaultType.ARRIVAL_SENSOR_FAIL) {
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Fault detected: ARRIVAL SENSOR FAIL " + efEvent);
			System.out.println("shutting down elevator...");
			
			ElevatorArriveEvent retry = new ElevatorArriveEvent(efEvent.getCar(), 1, elevator.getWorkingDirection());
			floorQueue.addLast(retry);
		}
		else if (efEvent.getFaultType() == FaultType.MOTOR_FAIL) {
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Fault detected: MOTOR FAIL " + efEvent);
			System.out.println("shutting down elevator...");
			
			elevator.setStatus(ElevatorJobState.OUT_OF_ORDER);
		}
	}
	
	/**
	 * General method to handle events on the scheduler, breaks off into specific event types
	 * @param event - generic event pulled from scheduler bounded buffer queue
	 */
	private void handleEvent(Event event) {
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: handling event " + event.getType());
		
		this.setState(State.HANDLING);
		if(Configuration.VERBOSE) {
			System.out.println("\t\tSCHEDULER: state change " + this.state + " ->HANDLING");
		}
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
			case FAULT:
				// send faults to the elevator (this is unlogged because it should be as if the elevator)
				// received the fault and then talks to the scheduler about it after (through other event types)
				elevatorQueue.addLast(event);
				handleElevatorFaultEvent((Fault) event);
				break;
			default:
				System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: Unhandled event " + event);
		}
		event.setSeen();
		
		this.setState(State.WAITING);
		if(Configuration.VERBOSE) {
			System.out.println("\t\tSCHEDULER: state change " + this.state + " ->WAITING");
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
			case WAITING:
				s = "Waiting State";
				break;
			case HANDLING:
				s = "Event Handling State";
				break;
			default:
				s = "Unknown State";
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
	
	public static void main(String[] args) {
		// create boundedbuffers as normal
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	
    	// setup the out queues
    	BoundedBuffer[] outQueues = new BoundedBuffer[2];
    	outQueues[0] = floorQueue;
    	outQueues[1] = elevatorQueue;
    	
    	// setup the out ports
    	int[] portsToSend = new int[2];
    	portsToSend[0] = Configuration.FLOOR_PORT;
    	portsToSend[1] = Configuration.ELEVATOR_PORT;
    	
    	// setup the in ports
    	int[] portsToReceive = new int[2];
    	portsToReceive[0] = Configuration.SCHEDULER_LISTEN_FLOOR_PORT;
    	portsToReceive[1] = Configuration.SCHEDULER_LISTEN_ELEVATOR_PORT;
		
        // scheduler needs a copy of all three queues
        Thread scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));
        
        // get the rpc thread running
        Thread rpcHandler = new Thread(new RpcHandler(schedulerQueue, outQueues, portsToSend, portsToReceive));
		
        scheduler.start();
        rpcHandler.start();
	}
	
}
