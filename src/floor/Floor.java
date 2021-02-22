package floor;
import java.util.HashMap;
import java.util.HashSet;

import event.DirectionType;
import event.FloorButtonPressEvent;
import main.Configuration;

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
	
	private HashMap<DirectionType, HashSet<Integer>> buttons;
	
	/**
	 * Create a new floor object
	 * @param f - floor number
	 */
	public Floor(int f) {
		// maybe should check here max sure it's in range
		floorNum = f;
		buttons = new HashMap<>();
		
		// floor will either have both directions or just one (top and bottom floors)
		if (floorNum != Configuration.NUM_FLOORS) {
			upButton = new FloorButton(DirectionType.UP);
			upLamp = new FloorLamp(DirectionType.UP);
			buttons.put(DirectionType.UP, new HashSet<>());
		}
		if (floorNum != 1) {
			downButton = new FloorButton(DirectionType.DOWN);
			downLamp = new FloorLamp(DirectionType.DOWN);
			buttons.put(DirectionType.DOWN, new HashSet<>());
		}
	}
	
	/**
	 * Request to go up is sent (this is like user clicking up button)
	 * @return status (success or not)
	 */
	public boolean requestUp(int destination) {
		// add to the list of people going up
		System.out.println(String.format("FLOOR %d: %d Added to floor destinations UP", floorNum, destination));
		buttons.get(DirectionType.UP).add(destination);
		
		// check to see if this request should be sent to the scheduler
		if (!upButton.getIsPressed()) {
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
	public boolean requestDown(int destination) {
		// add to the list of people going up
		System.out.println(String.format("FLOOR %d: %d Added to floor destinations DOWN", floorNum, destination));
		buttons.get(DirectionType.DOWN).add(destination);
		
		// make sure this floor can request going down
		if (!downButton.getIsPressed()) {
			downButton.setIsPressed(true);
			downLamp.setIsLit(true);
			return true;
		}
		return false;
	}
	
	
	public boolean requestDirection(FloorButtonPressEvent e) {
		System.out.println(String.format("FLOOR %d: %s Button Pressed", floorNum, e.getDirection()));
		if (e.getDirection() == DirectionType.UP) {
			return requestUp(e.getDestination());
		}
		return requestDown(e.getDestination());
	}
	
	/**
	 * What to do on elevator arrival, empties the set of destinations and return them
	 * @param dir - DirectionType UP or DOWN
	 * @return the list of floor destinations for that direction
	 */
	public Integer[] elevatorArrived(DirectionType dir) {
		if (floorNum == 1) {
			dir = DirectionType.UP;
		} else if (floorNum == Configuration.NUM_FLOORS) {
			dir = DirectionType.DOWN;
		}
		System.out.println(String.format("FLOOR %d: Elevator arrived, going %s", floorNum, dir));
		Integer[] elevatorButtons = new Integer[buttons.get(dir).size()];
		
		
		// return all the buttons of waiting people
		buttons.get(dir).toArray(elevatorButtons);
		buttons.get(dir).clear();
		
		// fix lights (no need to sort destinations)
		if (dir == DirectionType.DOWN) {
			downButton.setIsPressed(false);
			downLamp.setIsLit(false);
		} else {
			upButton.setIsPressed(false);
			upLamp.setIsLit(false);
		}
		
		return elevatorButtons;
	}
	
	/**
	 *  Getter method which returns down lamp
	 * @return the down lamp
	 */
	public FloorLamp getDownLamp() {
		return downLamp;
	}
	/**
	 *  Getter method which returns up lamp
	 * @return the up lamp
	 */
	public FloorLamp getUpLamp() {
		return upLamp;
	}
	/**
	 *  Getter method which returns down button
	 * @return the down button
	 */
	public FloorButton getDownButton(){
		return downButton;
	}
	/**
	 *  Getter method which returns up button
	 * @return the up button
	 */
	public FloorButton getUpButton(){
		return upButton;
	}
}
