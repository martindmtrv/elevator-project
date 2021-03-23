package scheduler;

import event.ElevatorTravelTimeoutEvent;
import event.ElevatorTripUpdateEvent;
import event.Event;
import event.EventType;
import main.Configuration;

/**
 * A timer class to represent travel timeouts on the elevator
 * Used to track MOTOR_FAIL or ARRIVAL_SENSOR_FAIL so the scheduler can shut down
 * the malfunctioning elevator
 * @author Martin Dimitrov
 *
 */
public class ElevatorEventTimer implements Runnable {
	private Event event;
	private int id;
	private BoundedBuffer schedulerQueue;
	private long timeDelay;
	
	/**
	 * Create a timer for elevator timeouts
	 * @param e - event we are waiting on timing out
	 * @param sched - bounded buffer for the scheduler
	 * @param i - id of the elevator we are timing
	 */
	public ElevatorEventTimer(Event e, BoundedBuffer sched, int i) {
		event = e;
		schedulerQueue = sched;
		id = i;
		
		// calculate the time to wait
		if (event.getType() == EventType.ELEVATOR_CALLED) {
			timeDelay = Configuration.TRAVEL_TIME_BETWEEN_FLOOR / 2 + Configuration.TIMEOUT_DELAY;
		} else if (event.getType() == EventType.ELEVATOR_TRIP_UPDATE) {
			if (((ElevatorTripUpdateEvent) event).getUpdate() == ElevatorTripUpdate.CONTINUE) {
				timeDelay = Configuration.TRAVEL_TIME_BETWEEN_FLOOR + Configuration.TIMEOUT_DELAY;
			} else {
				timeDelay = Configuration.TRAVEL_TIME_BETWEEN_FLOOR / 2 + Configuration.TIMEOUT_DELAY;
			}
			
		}
	}

	@Override
	public void run() {
		try {
			// after the specified delay notify the scheduler
			Thread.sleep(timeDelay);
			schedulerQueue.addLast(new ElevatorTravelTimeoutEvent(id));
		} catch(InterruptedException e) {
			// if the scheduler interrupts me that means stop the timer (this is done)
			return;
		}
		
		
	}
}
