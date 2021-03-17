package event;

import elevator.*;
/**
 * This event sends an update with the car ID number and the current state of the elevator.
 * @author Alex Cameron
 *
 */
public class ElevatorFaultUpdateEvent extends Event {

	private static final long serialVersionUID = -4004363160507796061L;
	private int car;
	private ElevatorState status;
    /**
     * This is an event to notify the elevator before reaching a floor if the car
     * should prepare to either continue or stop before reaching the next floor
     * @param c - car number
     * @param status - state of the elevator
     */
    public ElevatorFaultUpdateEvent(int c, ElevatorState status) {
        super("", EventType.ELEVATOR_FAULT_UPDATE);
        this.car = c;
        this.status = status;
    }
    
	public int getCar() {
		return car;
	}
	
	
	public ElevatorState getStatus() {
		return status;
	}
}
