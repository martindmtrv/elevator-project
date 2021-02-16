package scheduler;

import java.util.ArrayList;

import event.DirectionType;

/**
 * Class to keep track of elevator states from the scheduler side
 * @author Martin Dimitrov
 */
public class ElevatorStatus {
	private int id;
	private int location;
	private DirectionType direction;
	private ArrayList<Integer> destinations;
	
	/**
	 * Create an elevator status
	 * @param i - id of this elevator
	 */
	public ElevatorStatus(int i) {
		id = i;
		location = 0;
		direction = DirectionType.STILL;
		destinations = new ArrayList<>();
	}
	
	public ArrayList<Integer> getDestinations() {
		return destinations;
	}
	public DirectionType getDirection() {
		return direction;
	}
	public void setDirection(DirectionType direction) {
		this.direction = direction;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}
	
	
	
	
}
