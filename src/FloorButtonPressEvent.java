
public class FloorButtonPressEvent extends Event {
	private int floor;
	private int destination;
	private DirectionType direction;
	
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
}
