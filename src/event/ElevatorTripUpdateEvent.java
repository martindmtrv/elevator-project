package event;

import scheduler.ElevatorTripUpdate;

/**
 * This event sends an update to a particular elevator alerting whether or not it should continue or stop
 * when it reaches next floor.
 * Author: Alex Cameron
 */
public class ElevatorTripUpdateEvent extends Event {
	private static final long serialVersionUID = 8596629253620541259L;
	private int car;
    private int approachingFloor;
    private ElevatorTripUpdate update;

    /**
     * This is an event to notify the elevator before reaching a floor if the car
     * should prepare to either continue or stop before reaching the next floor
     * @param c - car number
     * @param af - floor we are approaching
     * @param u - the trip update (either CONTINUE or STOP)
     */
    public ElevatorTripUpdateEvent(int c, int af, ElevatorTripUpdate u) {
        super("", EventType.ELEVATOR_TRIP_UPDATE);
        approachingFloor = af;
        car = c;
        update = u;
    }

	public ElevatorTripUpdate getUpdate() {
		return update;
	}

	public int getCar() {
		return car;
	}

	public int getApproachingFloor() {
		return approachingFloor;
	}
}
