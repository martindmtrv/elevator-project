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
		elevatorEvents = new BoundedBuffer();
		schedulerEvents = new BoundedBuffer();
		
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
		// MOCK EVEVATOR ARRIVE EVENT to floor 3 (going down)
		ElevatorArriveEvent r = new ElevatorArriveEvent(1, 3, DirectionType.DOWN);
		elevatorEvents.addLast(r);
		
		boolean notPressed;
		Integer[] elevatorButtons;
		Event reply, event;
		
		FloorButtonPressEvent fbEvent;
		ElevatorArriveEvent eaEvent;
		
		// run until stopped
		while(!Thread.interrupted()) {
			event = (Event) events.removeFirst();
			
			if (event.getType() == EventType.FLOOR_BUTTON) {
				// send to scheduler after setting buttons of that
				fbEvent = (FloorButtonPressEvent) event;
				notPressed = floors[fbEvent.getFloor() - 1].requestDirection(fbEvent);
				
				// if notPressed is false that means button was already clicked (no need to request an elevator)
				if (notPressed) {
					schedulerEvents.addLast(fbEvent);
				}
			} else if (event.getType() == EventType.ELEVATOR_ARRIVED) {
				eaEvent = (ElevatorArriveEvent) event;
				elevatorButtons = floors[eaEvent.getFloor()-1].elevatorArrived(eaEvent.getDirection());
				
				reply = new ElevatorButtonPressEvent(elevatorButtons, eaEvent.getCar());
				
				schedulerEvents.addLast(reply);
				
				
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		Thread e = new Thread(new ElevatorSubsysem(Configuration.NUM_CARS, Configuration.NUM_FLOORS, elevatorQueue, schedulerQueue));
		e.start();
	}
	
}