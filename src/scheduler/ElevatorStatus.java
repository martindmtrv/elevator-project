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

	/**
	 * Returns nearest floor based on current floor, direction and lists of destinations
	 * @param direction Direction elevator is going in
	 * @param currentFloor current floor of elevator
	 * @return
	 */
	public int getNearestFloor(DirectionType direction, int currentFloor){
		int nearestDestination = -1;
		for(Integer i : destinations){
			if(direction == DirectionType.DOWN){ //find nearest floor if going down
				if (i < currentFloor) {
					if(nearestDestination == -1){
						nearestDestination = i;
					}else if(nearestDestination<i) {
						nearestDestination = i;
					}
				}
			}else{ //going up
				if(i>currentFloor){
					if(nearestDestination == -1){
						nearestDestination = i;
					}else if(nearestDestination > i)
					nearestDestination = i;
				}
			}
		}
		return nearestDestination;
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
