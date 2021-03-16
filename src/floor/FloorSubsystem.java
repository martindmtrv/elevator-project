package floor;
import java.util.Arrays;

import event.ElevatorArriveEvent;
import event.ElevatorButtonPressEvent;
import event.Event;
import event.FloorButtonPressEvent;
import main.Configuration;
import rpc.RpcHandler;
import scheduler.BoundedBuffer;

/**
 * Floor Subsystem for the elevator project. Controls all the floors and handles sending input to the scheduler
 * @author Martin Dimitrov
 */
public class FloorSubsystem implements Runnable {
	private Floor[] floors;
	private BoundedBuffer events;
	private BoundedBuffer schedulerEvents;
	
	/**
	 * Create a new FloorSubsystem 
	 * @param n - number of floors
	 * @param myQueue - floor queue
	 * @param sQueue - scheduler queue
	 */
	public FloorSubsystem(int n, BoundedBuffer myQueue, BoundedBuffer sQueue) {
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
			//Add floor state to event
			fbEvent.setState(floors[fbEvent.getFloor() - 1]);
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
		
		System.out.println("["+Event.getCurrentTime()+"]\tFLOORSUBSYSTEM: sending destinations " + Arrays.toString(elevatorButtons));
		reply = new ElevatorButtonPressEvent(elevatorButtons, eaEvent.getCar(), eaEvent.getDirection());
		
		//Add floor state to event
		reply.setState(floors[eaEvent.getFloor()-1]);
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
				// when I receive an unhandled event ie. a fault, I should simply pass it along to scheduler
				schedulerEvents.addLast(event);
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
	
	public static void main(String[] args) {
		// create boundedbuffers as normal
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	
    	// setup the out queues
    	BoundedBuffer[] outQueues = new BoundedBuffer[1];
    	outQueues[0] = schedulerQueue;
    	
    	// setup the out ports
    	int[] portsToSend = new int[1];
    	portsToSend[0] = Configuration.SCHEDULER_LISTEN_FLOOR_PORT;
    	
    	// setup the int ports
    	int[] portsToReceive = new int[1];
    	portsToReceive[0] = Configuration.FLOOR_PORT;
		       
        // floor needs a copy of all 2 queues
    	Thread floor = new Thread(new FloorSubsystem(Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");
    	
    	// InputStream gets the floors queue
        Thread inputstream = new Thread(new InputStream(Configuration.TEST_FILE, floorQueue));
        
        // get the rpc thread running
        Thread rpcHandler = new Thread(new RpcHandler(floorQueue, outQueues, portsToSend, portsToReceive));
		
        floor.start();
        rpcHandler.start();
        inputstream.start();
	}
}