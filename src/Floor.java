
/**
 * @author Martin Dimitrov
 *
 */
public class Floor implements Runnable {
	static final int MAX_FLOOR = 7, MIN_FLOOR = 1;
	
	private String[] input; /* string input for now */
	
	private int floorNum;
	
	private FloorButton upButton;
	private FloorButton downButton;
	
	private FloorLamp upLamp;
	private FloorLamp downLamp;
	
	
	/**
	 * Create a new floor object
	 * @param f
	 */
	Floor(int f) {
		// maybe should check here max sure it's in range
		floorNum = f;
		
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
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// wait for input
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				break;
			}
			
			// do stuff
			
			
			
		}
		
	}
}
