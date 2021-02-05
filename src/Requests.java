import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Requests {
	private String requestTime;
	private boolean requestUp;
	private boolean requestDown;
	private String destination;
	private String floor;
	private int requestType;
	private int i;
	
	/**
	 * This will generate a request whenever a button is pressed
	 * 
	 * @param req String value of the line read in from file
	 * @param type integer that will correspond to the enum of the request type
	 */
	public Requests(String req, int type) {
		try {
			List<String> tempList = Arrays.asList(req.split("\\s*,\\s*"));
			if(tempList.size() != 4) {
				throw new Exception("List length is: " + tempList.size() + ". Expects length of 4");
			}
			for(i=0; i < tempList.size(); i++) {
				switch(i) {
				case 0:
					this.requestTime = tempList.get(i);
				case 1:
					this.floor = tempList.get(i);
				case 2:
					if(tempList.get(i) == "UP") {
						this.requestUp = true;
					}
					else if(tempList.get(i) == "DOWN") {
						this.requestDown = true;
					}
					else {
						System.out.println("Invalid Direction");
					}
				case 3:
					this.destination = tempList.get(i);
				default:
					System.out.println("Invalid String");
				}
			}
		} catch(Exception e) {
		}
		this.requestType = type;
	}
	/**
	 * This will generate a request whenever a button is pressed 
	 * 
	 * @param req Array value of the request read in from file
	 * @param type integer corresponding to the enum of the request type
	 */
	public Requests(ArrayList<String> req, int type) {
		try {
			if(req.size() != 4) {
				throw new Exception("List length is: " + req.size() + ". Expects length of 4");
			}
			for(i=0; i < req.size(); i++) {
				switch(i) {
				case 0:
					this.requestTime = req.get(i);
				case 1:
					this.floor = req.get(i);
				case 2:
					if(req.get(i) == "UP") {
						this.requestUp = true;
					}
					else if(req.get(i) == "DOWN") {
						this.requestDown = true;
					}
					else {
						System.out.println("Invalid Direction");
					}
				case 3:
					this.destination = req.get(i);
				default:
					System.out.println("Invalid String");
				}
			}
		} catch(Exception e) {
		}
		this.requestType = type;
	}
	
	
	
}
