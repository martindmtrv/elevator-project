/**
 * Scheduler thread that handles the communication between the elevator thread and the floor thread
 * @author Erdem Yanikomeroglu
 */
public class Scheduler {

	private String request = null;
	public BoundedBuffer requestQueue;
	
	public Scheduler(String request) {
		this.request = request;
	}
	
	public synchronized void submitRequest(String task) {
		
		request = task;
		requestQueue.addLast(request);
		notifyAll();
	}
	
	public synchronized String assignTask() {

		
		request = (String)requestQueue.removeFirst();
		notifyAll();
		return request;
	}
}
