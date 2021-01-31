/**
 * Class representing a single floor. Will receive inputs from the FloorSubsystem to toggle its lights and buttons
 * on arrival of elevator and on input entered
 * @author Martin Dimitrov
 *
 */
public class Floor implements Runnable {
	static final int MAX_FLOOR = 10, MIN_FLOOR = 0;
	
	private int floorNum; /* The number of the floor */
	
	// floor buttons
	private FloorButton upButton;
	private FloorButton downButton;
	
	// floor lamps
	private FloorLamp upLamp;
	private FloorLamp downLamp;
	
	// buffer for this floors inputs (maybe will need a buffer for outputs?)
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
	 * @return status (success or not)
	 */
	public boolean requestUp() {
		// make sure this floor can request going up
		if (upButton != null && !upButton.getIsPressed()) {
			upButton.setIsPressed(true);
			upLamp.setIsLit(true);
			return true;
		}
		return false;
	}
	
	/**
	 * Request to go down is sent to the floor subsystem, (this is like user clicking down button)
	 * @return status (success or not)
	 */
	public boolean requestDown() {
		// make sure this floor can request going down
		if (downButton != null && !downButton.getIsPressed()) {
			downButton.setIsPressed(true);
			downLamp.setIsLit(true);
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		String input;
		String[] split;
		
		while (!Thread.interrupted()) {
			input = (String) buffer.removeFirst();
			split = input.split(" ");
			
			// TODO: change with the data structure after
			if (split[2].equals("Down")) {
				requestDown();
			} else {
				requestUp();
			}
			
			System.out.println(String.format("FLOOR %d: Received Input %s", floorNum, input));
		}
		
	}
}
