
/**
 * @author Martin Dimitrov
 *
 */
public class Floor implements Runnable {
	static final int MAX_FLOOR = 10, MIN_FLOOR = 0;
	
	private int floorNum;
	
	private FloorButton upButton;
	private FloorButton downButton;
	
	private FloorLamp upLamp;
	private FloorLamp downLamp;
	
	private BoundedBuffer buffer;
	
	
	/**
	 * Create a new floor object
	 * @param f - floor number
	 * @param b - bounded buffer for this floor
	 */
	Floor(int f, BoundedBuffer b) {
		// maybe should check here max sure it's in range
		floorNum = f;
		buffer = b;
		
		// floor will either have both directions or just one (top and bottom floors)
		if (floorNum != MAX_FLOOR) {
			upButton = new FloorButton(FloorButton.UP);
			upLamp = new FloorLamp(FloorButton.UP);
		}
		if (floorNum != MIN_FLOOR) {
			downButton = new FloorButton(FloorButton.DOWN);
			downLamp = new FloorLamp(FloorButton.DOWN);
		}
	}
	
	/**
	 * What happens when a request to go up is sent (this is like user clicking up button)
	 * @return
	 */
	public boolean requestUp() {
		// make sure this floor can request going up
		if (upButton != null && !upButton.getIsPressed()) {
			upButton.press();
			upLamp.setIsLit(true);
			
			// TODO: send event to floorsubsystem
			
			return true;
		}
		return false;
	}
	
	/**
	 * Request to go down is sent to the floor subsystem, (this is like user clicking down button)
	 * @return
	 */
	public boolean requestDown() {
		// make sure this floor can request going down
		if (downButton != null && !downButton.getIsPressed()) {
			downButton.press();
			downLamp.setIsLit(true);
			
			// TODO: send event to floorsubsystem
			
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		
		while (!Thread.interrupted()) {
			String input = (String) buffer.removeFirst();
			
			System.out.println(String.format("FLOOR %d: Received Input %s", floorNum, input));
			
			// Should send to the scheduler here
			
			
			
		}
		
	}
}
