package event;

/**
 * Event to describe an elevator called at a floor
 * @author Ammar Tosun
 */
public class ElevatorCalledEvent extends Event {
	private int car;
	private int floor;
	private DirectionType direction;
	
	/**
	 * Create elevator arrive event
	 * @param c - int car number
	 * @param f - int floor elevator is called at
	 * @param dir - DirectionType where it is going (UP/DOWN)
	 */
	public ElevatorCalledEvent(int c, int f, DirectionType dir) {
		super("", EventType.ELEVATOR_CALLED);
		car = c;
		floor = f;
		direction = dir;
	}
	
	public int getCar() {
		return car;
	}

	public int getFloor() {
		return floor;
	}

	public DirectionType getDirection() {
		return direction;
	}
}
