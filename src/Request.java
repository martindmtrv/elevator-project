
public class Request {
	private String requestTime;
	private DirectionType direction;
	private int destination;
	private int floor;
	private EventType requestType;
	private Integer[] elevatorButtons;
	
	/**
	 * This will generate a request FROM the txt file input
	 * 
	 * @param req String value of the line read in from file
	 * @param type integer that will correspond to the enum of the request type
	 */
	public Request(String req, EventType type) {
		String tempList[] = req.split(" ");
		this.setRequestTime(tempList[0]);
		this.setFloor(Integer.parseInt(tempList[1]));
		// get direction from enum
		this.setDirection(DirectionType.valueOf(tempList[2].toUpperCase()));
		this.setDestination(Integer.parseInt(tempList[3]));
		this.requestType = type;
	}
	
	/**
	 * for single event requests (rest is built through setters for what the Request needs)
	 * @param type
	 */
	public Request(EventType type) {
		this.requestType = type;
	}
	
	public String toString() {
		return String.format("%s %s %d %s %d", requestType, requestTime, floor, direction, destination);
	}
	
	public EventType getRequestType() {
		return this.requestType;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public DirectionType getDirection() {
		return direction;
	}

	public void setDirection(DirectionType direction) {
		this.direction = direction;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public Integer[] getElevatorButtons() {
		return elevatorButtons;
	}

	public void setElevatorButtons(Integer[] elevatorButtons) {
		this.elevatorButtons = elevatorButtons;
	}
}
