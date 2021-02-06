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
		// MOCK ELEVATOR CALLED EVENT to floor 3
		//ElevatorCalledEvent r = new ElevatorCalledEvent(1, 3, DirectionType.DOWN);
		//elevatorEvents.addLast(r);
		
		boolean notPressed;
		Integer[] elevatorButtons;
		Event reply, event;
		
		FloorButtonPressEvent fbEvent;
		ElevatorArriveEvent eaEvent;
		
		// run until stopped
		while(!Thread.interrupted()) {
			event = (Event) elevatorEvents.removeFirst();
			System.out.println(event);
			if (event.getType() == EventType.ELEVATOR_BUTTONS) {
				//when the elevator is on the floor that is called
				//get the destinations
				//sort the destinations
				//loop through destinations
				//visitFloor(int d)
				//send elevator arrived for each destination to the scheduler
				
			} 
			else if (event.getType() == EventType.FLOOR_BUTTON) {
				//elevator called from that floor
				//check the floor number that car is called
				//move the car to that floor
				//send elevator arrived to the scheduler
			}
		}
		
	}
	
	public static void main(String[] args) {
		Thread e = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR ,new BoundedBuffer(), new BoundedBuffer()));
		e.start();
	}
	
}