package event;

/**
 * Event to describe an elevator called to move
 * @author Ammar Tosun
 */
public class ElevatorCallToMoveEvent extends Event {
	private static final long serialVersionUID = 6048675164479301938L;
	private int car;
	private DirectionType direction;
	private Integer[] destinationToLight;
	
	/**
	 * Create elevator call to move event
	 * @param c - int car number
	 * @param dir - DirectionType where it is going (UP/DOWN)
	 */
	public ElevatorCallToMoveEvent(int c, DirectionType dir) {
		super("", EventType.ELEVATOR_CALLED);
		car = c;
		direction = dir;
		destinationToLight = new Integer[0];
	}
	
	/**
	 * Create elevator call to move event
	 * @param c - car number
	 * @param dir - directiontype
	 * @param i - destinations
	 */
	public ElevatorCallToMoveEvent(int c, DirectionType dir, Integer[] destinations) {
		this(c, dir);
		destinationToLight = destinations;
	}
	
	public int getCar() {
		return car;
	}

	public DirectionType getDirection() {
		return direction;
	}

	public Integer[] getDestinationToLight() {
		return destinationToLight;
	}
}
