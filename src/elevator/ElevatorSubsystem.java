package elevator;
import java.util.Arrays;

import event.*;
import main.Configuration;
import scheduler.BoundedBuffer;

/**
 * Elevator Subsystem for the elevator project. Controls all the elevators and handles sending input to the scheduler
 * @author Ammar Tosun (I1) & Alex Cameron (I2)
 */
public class ElevatorSubsystem implements Runnable {
	
	private Elevator[] elevators;
	private Thread[] elevatorThreads;
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
		elevatorThreads = new Thread[n];
		elevatorEvents = elevatorQueue;
		schedulerEvents = schedulerQueue;

		this.box = box;

		// create elevators
		for (int x = 0; x < n; x++) {
			elevators[x] = new Elevator(x, f, c, this.box, this.schedulerEvents);
			elevatorThreads[x] = new Thread(elevators[x], "elevator" + x); //Array of each elevator thread
		}
	}

	/**
	 * Start the elevator threads.
	 */
	public void runElevatorThreads(){
		for(int x = 0; x<elevatorThreads.length;x++){
			elevatorThreads[x].start();
		}
	}
	
	/**
	 * The elevator subsystem will receive inputs from scheduler
	 * For iteration 1 it will be read from a file
	 */
	@Override
	public void run() {
		Event event;
		ElevatorArriveEvent eaEvent;
		ElevatorCallToMoveEvent ectmEvent;
		ElevatorTripUpdateEvent etuEvent;
		runElevatorThreads(); //start elevator threads
		// run until stopped
		while (!Thread.interrupted()) {
			event = (Event) elevatorEvents.removeFirst();
			//new logic with iteration 2 scheduler implementations
			switch(event.getType()){
				case ELEVATOR_CALLED ->{
					ectmEvent = (ElevatorCallToMoveEvent) event; //Elevator shall move
					box.put(ectmEvent); //set direction of the car which the scheduler has assigned to move
				}
				case ELEVATOR_TRIP_UPDATE -> {
					etuEvent = (ElevatorTripUpdateEvent) event;
					box.put(etuEvent); //Notify elevators of and ElevatorTripUpdate
				}
				case ELEVATOR_APPROACH_SENSOR -> {
					ElevatorApproachSensorEvent easEvent = (ElevatorApproachSensorEvent) event;
					schedulerEvents.addLast(easEvent); //notify scheduler that arrival sensor triggered
				}
				case ELEVATOR_ARRIVED -> {
					eaEvent = (ElevatorArriveEvent) event;
					schedulerEvents.addLast(eaEvent); //forward elevator arrival event to scheduler from elevator
				}
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