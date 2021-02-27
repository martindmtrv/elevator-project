package floor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import event.DirectionType;
import event.ElevatorArriveEvent;
import event.ElevatorButtonPressEvent;
import event.Event;
import event.FloorButtonPressEvent;
import scheduler.BoundedBuffer;

/**
 * Floor Subsystem for the elevator project. Controls all the floors and handles sending input to the scheduler
 * @author Martin Dimitrov
 */
public class FloorSubsystem implements Runnable {
	private String inputFile;
	private Floor[] floors;
	private BoundedBuffer events;
	private BoundedBuffer schedulerEvents;
	
	/**
	 * Create a new FloorSubsystem 
	 * @param fp - path to input file
	 * @param n - number of floors
	 * @param myQueue - floor queue
	 * @param sQueue - scheduler queue
	 */
	public FloorSubsystem(String fp, int n, BoundedBuffer myQueue, BoundedBuffer sQueue) {
		inputFile = fp;
		floors = new Floor[n];
		events = myQueue;
		schedulerEvents = sQueue;
		
		// create all the floors and buffers
		for (int x = 0; x < n; x++) {
			floors[x] = new Floor(x+1);
		}
	}
	
	public int getNumFloors() {return floors.length;}
	
	/**
	 * Handler for floor button presses. It will request an elevator from the scheduler,
	 * if the button was not already pressed.
	 * @param fbEvent - the event to handle
	 */
	public void handleFloorButtonPressEvent(FloorButtonPressEvent fbEvent) {
		boolean notPressed;
		
		// send to scheduler after setting buttons of that
		notPressed = floors[fbEvent.getFloor() - 1].requestDirection(fbEvent);
		
		// if notPressed is false that means button was already clicked (no need to request an elevator)
		if (notPressed) {
			schedulerEvents.addLast(fbEvent);
		}
	}
	
	/**
	 * Handling of elevator arrival event. This means sending over the elevator button presses for the destinations
	 * requested by passengers
	 * @param eaEvent - the elevator arrive event to handle
	 */
	public void handleElevatorArriveEvent(ElevatorArriveEvent eaEvent) {
		Integer[] elevatorButtons;
		ElevatorButtonPressEvent reply;
		
		elevatorButtons = floors[eaEvent.getFloor()-1].elevatorArrived(eaEvent.getDirection());
		
		System.out.println("["+Event.getRequestTime()+"]\tFLOORSUBSYSTEM: sending destinations " + Arrays.toString(elevatorButtons));
		reply = new ElevatorButtonPressEvent(elevatorButtons, eaEvent.getCar(), eaEvent.getDirection());
		
		schedulerEvents.addLast(reply);
	}
	
	/**
	 * General method with switch case for all event types handled by this system
	 * @param event - the event to handle
	 */
	public void handleEvent(Event event) {
		switch (event.getType()) {
			case FLOOR_BUTTON: {
				handleFloorButtonPressEvent((FloorButtonPressEvent) event);
				break;
			}
			case ELEVATOR_ARRIVED: {
				handleElevatorArriveEvent((ElevatorArriveEvent) event);
				break;
			}
			default:
				break;
			}
	}
	
	@Override
	public void run() {

		Event event;
		
		
		// run until stopped
		while(!Thread.interrupted()) {
			event = (Event) events.removeFirst();
			handleEvent(event);
		}
	}
}