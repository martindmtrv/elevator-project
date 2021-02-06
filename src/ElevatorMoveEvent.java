/**
 * Event to represent Elevator moving
 * @author Ammar Tosun
 */
public class ElevatorMoveEvent extends Event {

	private int car;
	private int floor;
	private int destination;
	private DirectionType direction;
	
	/**
	 * Create elevator move event
	 * @param c - car number
	 * @param f - current floor number
	 * @param d - destination floor number
	 * @param dir - DirectionType UP or DOWN
	 */
	public ElevatorMoveEvent(int c, int f, int d, DirectionType dir) {
		super("", EventType.ELEVATOR_MOVING);
		car = c;
		floor = f;
		destination = d;
		direction = dir;
	}

	//getters for the data members
	public int getCar() { return car; }
	public int getFloor() { return floor; }
	public DirectionType getDirection() { return direction; }
	public int getDestination() { return destination;}
}
