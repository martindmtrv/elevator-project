package event;

import floor.Floor;
/**
 * Event to describe an elevator arriving at a floor
 * @author Martin Dimitrov
 */
public class ElevatorArriveEvent extends Event {
	private int car;
	private int floor;
	private DirectionType direction;
	private Floor currentState;
	
	/**
	 * Create elevator arrive event
	 * @param c - int car number
	 * @param f - int floor it arrived at
	 * @param dir - DirectionType where it is going (UP/DOWN)
	 */
	public ElevatorArriveEvent(int c, int f, DirectionType dir) {
		super("", EventType.ELEVATOR_ARRIVED);
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
	public void setState(Floor state) {
		currentState = state;
	}
	public Floor getState() {
		return currentState;
	}
}
