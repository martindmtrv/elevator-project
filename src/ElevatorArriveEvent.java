
public class ElevatorArriveEvent extends Event {
	private int car;
	private int floor;
	private DirectionType direction;
	
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
}
