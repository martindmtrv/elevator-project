package scheduler;
import event.Event;

/**
 * Scheduler thread that handles the communication between the elevator thread and the floor thread.
 * Currently, it passes requests from the elevator thread to the floor thread and vice versa on a first-in,
 * first-out basis. The scheduler's logic will be upgraded in upcoming iterations.
 * @author Erdem Yanikomeroglu
 */
public class Scheduler implements Runnable {
	public BoundedBuffer schedulerQueue;
	private BoundedBuffer elevatorQueue;
	private BoundedBuffer floorQueue;
	
	/**
	 * Create a new scheduler object
	 * @param schedulerQueue
	 * @param elevatorQueue
	 * @param floorQueue
	 */
	public Scheduler(BoundedBuffer schedulerQueue, BoundedBuffer elevatorQueue, BoundedBuffer floorQueue) {
		this.schedulerQueue = schedulerQueue;
		this.elevatorQueue = elevatorQueue;
		this.floorQueue = floorQueue;
	}
	
	@Override
	public void run() {
		Event event_to_send;
		
		while(!Thread.interrupted()) {
			event_to_send = (Event)schedulerQueue.removeFirst();
			// route the event to the right place
			switch(event_to_send.getType()) {
				case FLOOR_BUTTON:
				case ELEVATOR_BUTTONS:
					System.out.println("SCHEDULER: Sending event " + event_to_send + " to elevator");
					elevatorQueue.addLast(event_to_send);
					break;
				case ELEVATOR_ARRIVED:
					System.out.println("SCHEDULER: Sending event " + event_to_send + " to floor");
					floorQueue.addLast(event_to_send);
					break;
				default:
					System.out.println("SCHEDULER: Unhandled event " + event_to_send);
			}
			
		}
	}
}
