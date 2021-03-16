package event;

import floor.Floor;

/**
 * Event to represent Floor button presses
 * @author Martin Dimitrov
 */
public class FloorButtonPressEvent extends Event {
	private static final long serialVersionUID = -333622461205588683L;
	private int floor;
	private int destination;
	private DirectionType direction;
	private Floor currentState;
	
	/**
	 * 
	 * @param time - time string (available for input file)
	 * @param f - floor number
	 * @param d - int destination floor
	 * @param dir - DirectionType UP or DOWN
	 */
	public FloorButtonPressEvent(String time, int f, int d, DirectionType dir) {
		super(time, EventType.FLOOR_BUTTON);
		floor = f;
		direction = dir;
		destination = d;
	}

	public int getFloor() {
		return floor;
	}

	public DirectionType getDirection() {
		return direction;
	}
	
	public int getDestination() {
		return destination;
	}
	
	public Floor getState() {
		return currentState;
	}
	public void setState(Floor state) {
		this.currentState = state;
	}
}
