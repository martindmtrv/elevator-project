/**
 * Scheduler thread that handles the communication between the elevator thread and the floor thread
 * @author Erdem Yanikomeroglu
 */
public class Scheduler {
	
	private boolean empty = true;
	private String request = null;
	
	// Testing comment
	
	public Scheduler(boolean empty, String request) {
		this.empty = empty;
		this.request = request;
	}
	
	public synchronized void submitRequest(String task) {
		while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
		request = task;
		empty = false;
		notifyAll();
	}
	
	public synchronized String assignTask() {
		while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
		String task = request;
		request = null;
		empty = true;
		notifyAll();
		return request;
	}
}
