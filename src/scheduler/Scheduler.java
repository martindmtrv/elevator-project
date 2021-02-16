package scheduler;
import java.util.ArrayList;
import java.util.Arrays;

import event.DirectionType;
import event.ElevatorButtonPressEvent;
import event.ElevatorCallToMoveEvent;
import event.Event;
import event.FloorButtonPressEvent;
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
			if (e.getDirection() == DirectionType.STILL) {
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
		ArrayList<Integer> destinations;
		for (ElevatorStatus e: elevators) {
			destinations = e.getDestinations();
			if (d == DirectionType.UP && d == e.getDirection() && pickup > e.getLocation()) {
				// maintaining sorted
				if (pickup == Configuration.NUM_FLOORS) {
					// add to the end
					if (destinations.size() == 0 || destinations.get(destinations.size() - 1) != pickup) {
						destinations.add(pickup);
					}
				} else {
					// insert in the right position
					for (int i=0; i < destinations.size(); i++) {
						if (pickup < destinations.get(i)) {
							destinations.add(i, pickup);
							break;
						}
					}
				}
				return e.getId();
			} else if (d == DirectionType.DOWN && d == e.getDirection() && pickup < e.getLocation()) {				
				// maintaining sorted
				if (pickup == 1) {
					// add to the end
					if (destinations.size() == 0 || destinations.get(0) != pickup) {
						destinations.add(0, pickup);
					}
				} else {
					// insert in the right position
					for (int i=0; i < destinations.size(); i++) {
						if (pickup > destinations.get(i)) {
							destinations.add(i, pickup);
							break;
						}
					}
				}
				return e.getId();
			}
		}
		return -1;
	}
	
	@Override
	public void run() {
		Event event;
		FloorButtonPressEvent buttonEv;
		ElevatorButtonPressEvent elevatorBEv;
		ElevatorCallToMoveEvent elevatorRequest;
		ElevatorStatus elevator;
		int assigned;
		
		
		while(!Thread.interrupted()) {
			event = (Event)schedulerQueue.removeFirst();
			
			switch(event.getType()) {
				case FLOOR_BUTTON: {
					buttonEv = (FloorButtonPressEvent) event;
					// find a elevator on the way
					assigned = elevatorEnRouteAdd(buttonEv.getDirection(), buttonEv.getFloor());
					
					// if unsuccessful
					if (assigned == -1) {
						// assign to a free elevator
						elevator = findIdleElevator();
						
						// no idle elevators ... 
						if (elevator == null) {
							// leave in unassigned
							unscheduled.add(buttonEv);
						} else {
							elevatorRequest = new ElevatorCallToMoveEvent(elevator.getId(), buttonEv.getDirection());
							elevatorQueue.addLast(elevatorRequest);
						}
					}
					break;
					
				}
				case ELEVATOR_BUTTONS:
					elevatorBEv = (ElevatorButtonPressEvent) event;
					
					// update elevator state
					elevator = elevators.get(elevatorBEv.getCar());
					elevator.getDestinations().addAll(Arrays.asList(elevatorBEv.getButtons()));
					elevator.setDirection(elevatorBEv.getDirection());
					
					
					// get the elevator moving
					elevatorRequest = new ElevatorCallToMoveEvent(elevatorBEv.getCar(), elevatorBEv.getDirection());
					System.out.println("SCHEDULER: Sending event " + elevatorRequest + " to elevator");
					elevatorQueue.addLast(elevatorRequest);
					break;
				case ELEVATOR_ARRIVED:
					System.out.println("SCHEDULER: Sending event " + event + " to floor");
					floorQueue.addLast(event);
					break;
				case ELEVATOR_DOORS_OPEN:
					
					break;
				default:
					System.out.println("SCHEDULER: Unhandled event " + event);
			}
			
		}
	}
}
