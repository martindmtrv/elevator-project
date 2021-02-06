/**
 * Scheduler thread that handles the communication between the elevator thread and the floor thread.
 * Currently, it passes requests from the elevator thread to the floor thread and vice versa on a first-in,
 * first-out basis. The scheduler's logic will be upgraded in upcoming iterations.
 * @author Erdem Yanikomeroglu
 */
public class Scheduler {

	public BoundedBuffer schedulerQueue = new BoundedBuffer();
	private BoundedBuffer elevatorQueue = new BoundedBuffer();
	private BoundedBuffer floorQueue = new BoundedBuffer();
	
	public Scheduler(BoundedBuffer shedulerQueue, BoundedBuffer elevatorQueue, BoundedBuffer floorQueue) {
		this.schedulerQueue = schedulerQueue;
		this.elevatorQueue = elevatorQueue;
		this.floorQueue = floorQueue;
	}
	
	public synchronized void receiveEvent(Event request) {
		schedulerQueue.addLast(request);
		notifyAll();
	}
	
	public synchronized void assignEvent() {
		Event event_to_send = (Event)schedulerQueue.removeFirst();
		switch(event_to_send.getType()) {
		case FLOOR_BUTTON:
			elevatorQueue.addLast(event_to_send);
		case ELEVATOR_ARRIVED:
		case ELEVATOR_BUTTONS:
		case ELEVATOR_CALLED:
		case ELEVATOR_MOVING:
			floorQueue.addLast(event_to_send);
		default:
			schedulerQueue.addLast(event_to_send);
			System.out.println("Failed to dequeue request in scheduler\n");
		}
		notifyAll();
	}
}
