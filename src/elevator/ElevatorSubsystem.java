package elevator;

import event.*;
import main.Configuration;
import rpc.RpcHandler;
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

	private Box[] boxArr;
	
	/**
	 * Create a new ElevatorSubsystem 
	 * @param n - number of elevators/cars
	 * @param f - number of floors
	 * @param c - initial floor of the elevators
	 * @param elevatorQueue - events queue to be handled by the elevator subsystem 
	 * @param schedulerQueue - events to be read that come from the scheduler subsystem
	 */
	public ElevatorSubsystem(int n, int f, int c, BoundedBuffer elevatorQueue, BoundedBuffer schedulerQueue) {
		elevators = new Elevator[n];
		elevatorThreads = new Thread[n];
		elevatorEvents = elevatorQueue;
		schedulerEvents = schedulerQueue;
		boxArr = new Box[n];
		
		// create elevators
		for (int x = 0; x < n; x++) {
			boxArr[x] = new Box(); //each elevator car gets their own box class
			elevators[x] = new Elevator(x, f, c, boxArr[x], this.schedulerEvents);
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
		runElevatorThreads(); //start elevator threads
		// run until stopped
		while (!Thread.interrupted()) {
			event = (Event) elevatorEvents.removeFirst();
			//new logic with iteration 2 scheduler implementations
			switch(event.getType()){
				case ELEVATOR_CALLED:
					ElevatorCallToMoveEvent ectmEvent = (ElevatorCallToMoveEvent) event;
					boxArr[ectmEvent.getCar()].put(ectmEvent); //set direction of the car which the scheduler has assigned to move
				break;
				case ELEVATOR_TRIP_UPDATE:
					ElevatorTripUpdateEvent etuEvent = (ElevatorTripUpdateEvent) event;
					boxArr[etuEvent.getCar()].put(etuEvent); //Notify elevators of and ElevatorTripUpdate
				break;
				case ELEVATOR_APPROACH_SENSOR:
					schedulerEvents.addLast((ElevatorApproachSensorEvent) event); //notify scheduler that arrival sensor triggered
				break;
				case ELEVATOR_ARRIVED:
					schedulerEvents.addLast((ElevatorArriveEvent) event); //forward elevator arrival event to scheduler from elevator
				break;
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
		// create boundedbuffers as normal
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	
    	// setup the out queues
    	BoundedBuffer[] outQueues = new BoundedBuffer[1];
    	outQueues[0] = schedulerQueue;
    	
    	// setup out ports
    	int[] portsToSend = new int[1];
    	portsToSend[0] = Configuration.SCHEDULER_LISTEN_ELEVATOR_PORT;
    	
    	// setup in ports
    	int[] portsToReceive = new int[1];
    	portsToReceive[0] = Configuration.ELEVATOR_PORT;
		       
        // elevator needs a copy of 2 queues
    	Thread elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue), "elevator");
    	
        // get the rpc thread running
        Thread rpcHandler = new Thread(new RpcHandler(elevatorQueue, outQueues, portsToSend, portsToReceive));
		
        elevator.start();
        rpcHandler.start();
	}
}