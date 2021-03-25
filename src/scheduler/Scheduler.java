package scheduler;
import java.util.*;

import elevator.ElevatorState;
import event.*;
import floor.Floor;
import main.Configuration;
import rpc.RpcHandler;
import scheduler.GUI.NotificationType;
import scheduler.GUI.SchedulerView;
import scheduler.GUI.SchedulerViewListener;

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
	private Thread[] timers;
	private ArrayList<ElevatorStatus> elevators;
	private ArrayList<FloorButtonPressEvent> unscheduled;
	private List<SchedulerViewListener> schedulerViewListeners;
	private SchedulerView schedulerView;

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
		
		// create empty array of timers
		timers = new Thread[Configuration.NUM_CARS];

		//init riskViewListeners
		schedulerViewListeners = new ArrayList<>();

		schedulerView = new SchedulerView(this);
	}
	
	/**
	 * Business logic for the passenger pick up:
	 * Find the closest available elevator to pick up, 
	 * so that the waiting time for passengers at floors is minimized
	 * 
	 * @param dir - direction the elevator should be going
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
		String msg = String.format("Handling floor button event pickup %d direction %s", buttonEv.getFloor(), buttonEv.getDirection());
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " + msg);

		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
		
		elevator = findClosestElevator(buttonEv.getDirection(), buttonEv.getFloor());
		
		if (elevator == null) {		// no available elevator
			msg = "Placed in unassigned queue, going to wait for a free elevator";
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: "+ msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

			unscheduled.add(buttonEv);	
			return;
		}
		
		if (elevator.getStatus() == ElevatorJobState.EN_ROUTE ) {
			HashSet<Integer> destinations;
			destinations = elevator.getDestinations();
			msg = String.format("Found enroute elevator %d going %s adding new stop %d to its destinations", elevator.getId(), buttonEv.getDirection(), buttonEv.getFloor());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " + msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
			destinations.add(buttonEv.getFloor());
			// successfully assigned to enroute elevator
			msg =String.format("Assigned pickup at %d %s to elevator %d ENROUTE", buttonEv.getFloor(), buttonEv.getDirection(), elevator.getId());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " + msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
			return;
		}
		
		if (elevator.getStatus() == ElevatorJobState.IDLE) {
			msg =String.format("Elevator %d is IDLE sending request to move for pickup", elevator.getId());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

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
				
				// create and start a timer here (in case it doesnt get here)
				timers[elevator.getId()] = new Thread(new ElevatorEventTimer(elevatorRequest, schedulerQueue, elevator.getId()));
				timers[elevator.getId()].start();
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
		String msg = "Adding elevator button presses: " + Arrays.toString(elevatorBEv.getButtons()) + " to elevator " + elevatorBEv.getCar();
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " + msg);
		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

		elevator.getDestinations().addAll(Arrays.asList(elevatorBEv.getButtons())); //add all new elevator destinations
		//elevator.setDirection(elevatorBEv.getDirection());
		
		if (elevator.getStatus() == ElevatorJobState.FAULT) {
			msg = "Elevator " + elevatorBEv.getCar() + " has stuck doors currently (will not tell it to go yet)";
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " + msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
		} else if (elevator.getDestinations().size() > 0) {
			// update states
			elevator.setStatus(ElevatorJobState.EN_ROUTE);
			elevator.setWorkingDirection(elevatorBEv.getDirection());
			elevator.setDirection(elevatorBEv.getDirection());
			
			// get the elevator moving
			elevatorRequest = new ElevatorCallToMoveEvent(elevatorBEv.getCar(), elevator.getWorkingDirection(), elevatorBEv.getButtons());
			msg = "Sending event " + elevatorRequest + " to elevator";
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
			elevatorQueue.addLast(elevatorRequest);
		} else {
			elevator.setStatus(ElevatorJobState.IDLE);
			elevator.setDirection(DirectionType.STILL);
			elevator.setWorkingDirection(DirectionType.STILL);
			msg = String.format("Elevator %d is now IDLE", elevator.getId());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: "+msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

			// since an elevator is now free, try and schedule the unassigned events
			// call event handle with all unassigned to try and get them in
			unscheduledEvents = unscheduled.toArray();
			unscheduled.clear();
			
			if (unscheduledEvents.length > 0) {
				msg = "Free elevator detected, trying to schedule " + unscheduledEvents.length + " unassigned events";
				System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " + msg);
				//Update Notification Events
				schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
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
		String msg;
		if (elevator.getStatus() == ElevatorJobState.FAULT) {
			msg = String.format("Elevator %d is approaching but is in FAULT -->> Disregarding this", easEvent.getCar());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
			return;
		}
		// STOP THE TIMER!!
		timers[easEvent.getCar()].interrupt();
		
		ElevatorTripUpdateEvent etuEvent;
		int floorToReach;
		if (easEvent.getDirection() == DirectionType.DOWN) {
			floorToReach = elevator.getLocation() - 1;
		} else {
			floorToReach = elevator.getLocation() + 1;
		}
		msg = String.format("Elevator %d is approaching floor %d going %s", easEvent.getCar(), floorToReach, elevator.getDirection());
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

		// update the state
		elevator.setLocation(floorToReach);
		
		// check if this floor in the destinations OR is one of the limits (avoid crashing out)
		if (floorToReach == 1 || floorToReach == Configuration.NUM_FLOORS || elevator.getDestinations().contains(floorToReach)) {
			etuEvent = new ElevatorTripUpdateEvent(elevator.getId(), floorToReach, ElevatorTripUpdate.STOP);
		} else {
			etuEvent = new ElevatorTripUpdateEvent(elevator.getId(), floorToReach, ElevatorTripUpdate.CONTINUE);
		}
		msg = String.format("Sending trip update %s to elevator %d", etuEvent.getUpdate(), etuEvent.getCar());
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

		//handle elevatortripupdateevent NOTE: CHANGE LATER
		for(SchedulerViewListener schedulerViewListener : schedulerViewListeners){
			schedulerViewListener.handleElevatorStatusUpdate(etuEvent);
		}
		
		elevatorQueue.addLast(etuEvent); //add new ElevatorTripUpdateEvent to elevator's queue
		
		// create and start a timer here (in case it doesnt get here)
		timers[elevator.getId()] = new Thread(new ElevatorEventTimer(etuEvent, schedulerQueue, elevator.getId()));
		timers[elevator.getId()].start();
	}
	
	/**
	 * Handle event when elevator arrives
	 * @param eaEvent - event to handle
	 */
	private void handleElevatorArriveEvent(ElevatorArriveEvent eaEvent) {
		ElevatorStatus elevator = elevators.get(eaEvent.getCar());
		String msg;
		if (elevator.getStatus() == ElevatorJobState.FAULT) {
			msg = String.format("Elevator %d is approaching but is in FAULT -->> Disregarding this", eaEvent.getCar());
			System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
			//Update Notification Events
			schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
			return;
		}
		// STOP THE TIMER!! (may not be one in the case of elevator already on the floor it was called to)
		Thread timer = timers[eaEvent.getCar()];
		if (timer != null) {
			timer.interrupt();
		}
		msg = "Sending event " + eaEvent + " to floor";
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

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
	private void handleElevatorFaultUpdateEvent(ElevatorFaultUpdateEvent efuEvent) {
		ElevatorStatus elevator = elevators.get(efuEvent.getCar());
		elevator.setDirection(DirectionType.STILL);

		if (efuEvent.getStatus() == ElevatorState.FAULT) {
			elevator.setStatus(ElevatorJobState.FAULT);
		}
		else {
			elevator.setStatus(ElevatorJobState.PICKING_UP);
			
			ElevatorArriveEvent retry = new ElevatorArriveEvent(efuEvent.getCar(), elevator.getLocation(), elevator.getWorkingDirection());
			floorQueue.addLast(retry);
		}
	}
	
	/**
	 * Handle timer going off (meaning elevator did not reach sensor or update us in reasonable time)
	 * @param ettEvent - event to handle
	 */
	public void handleElevatorTravelTimeout(ElevatorTravelTimeoutEvent ettEvent) {
		// at this point we must assume it is stuck (MOTOR_FAIL or ARRIVAL_SENSOR_FAIL)
		// so we send a fault over there
		ElevatorStatus elevator = elevators.get(ettEvent.getCar());
		elevator.setStatus(ElevatorJobState.FAULT);
		String msg = "Elevator " + ettEvent.getCar() + " timed out! Sending fault";
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
		Fault fault = new Fault(ettEvent.getCar(), FaultType.ARRIVAL_SENSOR_FAIL);
		elevatorQueue.addLast(fault);
	}
	
	/**
	 * General method to handle events on the scheduler, breaks off into specific event types
	 * @param event - generic event pulled from scheduler bounded buffer queue
	 */
	private void handleEvent(Event event) {
		String msg =  "Handling event " + event.getType();
		System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER:" +msg);
		//Update Notification Events
		schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);

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
				break;
			case ELEVATOR_FAULT_UPDATE:
				handleElevatorFaultUpdateEvent((ElevatorFaultUpdateEvent) event);
				break;
			case ELEVATOR_TRAVEL_TIMEOUT:
				handleElevatorTravelTimeout((ElevatorTravelTimeoutEvent) event);
				break;
			default:
				msg = "Unhandled event " + event;
				System.out.println("["+Event.getCurrentTime()+"]\tSCHEDULER: " +msg);
				//Update Notification Events
				schedulerView.getNotificationView().notifyView(msg, Event.getCurrTime(), NotificationType.SCHEDULER);
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

	/**
	 * Add a scheduler listener to a list of listeners
	 * @param schedulerViewListener
	 */
	public void addSchedulerViewListeners(SchedulerViewListener schedulerViewListener){
		schedulerViewListeners.add(schedulerViewListener);
	}

	public SchedulerView getSchedulerView(){
		return schedulerView;
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
