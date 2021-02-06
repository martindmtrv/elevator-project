import java.util.Arrays;

/**
 * Elevator Subsystem for the elevator project. Controls all the elevators and handles sending input to the scheduler
 * @author Ammar Tosun
 */
public class ElevatorSubsystem implements Runnable {
	
	private Elevator[] elevators;				
	private BoundedBuffer elevatorEvents;
	private BoundedBuffer schedulerEvents;
	
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
		elevatorEvents = elevatorQueue;
		schedulerEvents = schedulerQueue;
		
		// create elevators
		for (int x = 0; x < n; x++) {
			elevators[x] = new Elevator(x, f, c);
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
		
		// run until stopped
		while(!Thread.interrupted()) {
			
			event = (Event) elevatorEvents.removeFirst();
			
			//when the elevator is on the floor that is called
			if (event.getType() == EventType.ELEVATOR_BUTTONS) {
				ebEvent = (ElevatorButtonPressEvent) event;
	
				//get the destinations
				Integer[] destinations = ebEvent.getButtons();
	
				//sort the destinations
				Arrays.sort(destinations);
				
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
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		Thread e = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR ,new BoundedBuffer(), new BoundedBuffer()));
		e.start();
	}
	
}