package event;

public class ElevatorTravelTimeoutEvent extends Event {

	private static final long serialVersionUID = -4074658383411677942L;
	private int car;
	
	public ElevatorTravelTimeoutEvent(int c) {
		super("", EventType.ELEVATOR_TRAVEL_TIMEOUT);
		car = c;
	}

	public int getCar() {
		return car;
	}
}
