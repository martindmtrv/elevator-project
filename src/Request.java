
public class Request {
	private String requestTime;
	private String direction;
	private int destination;
	private int floor;
	private RequestType requestType;
	
	/**
	 * This will generate a request whenever a button is pressed
	 * 
	 * @param req String value of the line read in from file
	 * @param type integer that will correspond to the enum of the request type
	 */
	public Request(String req, RequestType type) {
		String tempList[] = req.split(" ");
		this.setRequestTime(tempList[0]);
		this.setFloor(Integer.parseInt(tempList[1]));
		this.setDirection(tempList[2]);
		this.setDestination(Integer.parseInt(tempList[3]));
		this.requestType = type;
	}
	
	/**
	 * for single event requests
	 * @param type
	 */
	public Request(RequestType type) {
		this.requestType = type;
	}
	
	public String toString() {
		return String.format("EVENT: type %d", requestType);
	}
	
	public RequestType getRequestType() {
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
}
