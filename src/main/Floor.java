/**
 * Class representing a single floor. Will receive inputs from the FloorSubsystem to toggle its lights and buttons
 * on arrival of elevator and on input entered
 * @author Martin Dimitrov
 *
 */
public class Floor {	
	private int floorNum; /* The number of the floor */
	
	// floor buttons
	private FloorButton upButton;
	private FloorButton downButton;
	
	// floor lamps
	private FloorLamp upLamp;
	private FloorLamp downLamp;
	
	/**
	 * Create a new floor object
	 * @param f - floor number
	 */
	Floor(int f) {
		// maybe should check here max sure it's in range
		floorNum = f;
		
		// floor will either have both directions or just one (top and bottom floors)
		if (floorNum != Configuration.NUM_FLOORS) {
			upButton = new FloorButton(FloorButton.UP);
			upLamp = new FloorLamp(FloorButton.UP);
		}
		if (floorNum != 1) {
			downButton = new FloorButton(FloorButton.DOWN);
			downLamp = new FloorLamp(FloorButton.DOWN);
		}
	}
	
	/**
	 * Request to go up is sent (this is like user clicking up button)
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
	 * Request to go down (this is like user clicking down button)
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
}
