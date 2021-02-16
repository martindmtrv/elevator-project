package event;

/**
 * Event to describe an elevator called to move
 * @author Ammar Tosun
 */
public class ElevatorCallToMoveEvent extends Event {
	private int car;
	private DirectionType direction;
	
	/**
	 * Create elevator arrive event
	 * @param c - int car number
	 * @param dir - DirectionType where it is going (UP/DOWN)
	 */
	public ElevatorCallToMoveEvent(int c, DirectionType dir) {
		super("", EventType.ELEVATOR_CALLED);
		car = c;
		direction = dir;
	}
	
	public int getCar() {
		return car;
	}

	public DirectionType getDirection() {
		return direction;
	}
}
