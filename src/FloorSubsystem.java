import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Floor Subsystem for the elevator project. Controls all the floors and handles sending input to the scheduler
 * @author Martin Dimitrov
 */
public class FloorSubsystem implements Runnable {
	private String inputFile;
	private Floor[] floors;
	private BoundedBuffer events;
	private BoundedBuffer schedulerEvents;
	
	/**
	 * Create a new FloorSubsystem 
	 * @param fp - path to input file
	 * @param n - number of floors
	 */
	FloorSubsystem(String fp, int n, BoundedBuffer myQueue, BoundedBuffer sQueue) {
		inputFile = fp;
		floors = new Floor[n];
		events = myQueue;
		schedulerEvents = sQueue;
		
		// create all the floors and buffers
		for (int x = 0; x < n; x++) {
			floors[x] = new Floor(x+1);
		}
	}
	
	/**
	 * Read the input file in
	 * @param fp - the filepath of the input file
	 * @return list of the lines in the file
	 */
	public ArrayList<String> readInput(String fp) {
		try {
			ArrayList<String> lines = new ArrayList<>();
			File input = new File(fp);
			Scanner scan = new Scanner(input);
			
			// get all the lines
			while (scan.hasNextLine()) {
				lines.add(scan.nextLine());
			}
			
			scan.close();
			return lines;
		} catch (FileNotFoundException e) {
			System.out.println(String.format("Floor System: File %s not found", fp));
			return null;
		}
	}
	
	/**
	 * Convert one line of input into an Event
	 * @param s - line from the input file
	 * @return FloorButtonPressEvent
	 */
	public FloorButtonPressEvent parseLine(String s) {
		
		String[] input = s.split(" ");
		
		return new FloorButtonPressEvent(input[0], Integer.parseInt(input[1]), 
				Integer.parseInt(input[3]), DirectionType.valueOf(input[2].toUpperCase()));
		
	}
	
	@Override
	public void run() {
		// parse input
		ArrayList<String> file = readInput(inputFile);
		
		if (file == null) {
			return;
		}
		
		// add inputs to floor buffers
		for (String s: file) {
			// parse data
			// add to FloorSubsystem event queue
			events.addLast(parseLine(s));
		}
		
		
		
		// MOCK EVEVATOR ARRIVE EVENT to floor 3 (going down)
		ElevatorArriveEvent r = new ElevatorArriveEvent(1, 3, DirectionType.DOWN);
		events.addLast(r);
		
		
		boolean notPressed;
		Integer[] elevatorButtons;
		Event reply, event;
		
		FloorButtonPressEvent fbEvent;
		ElevatorArriveEvent eaEvent;
		
		// run until stopped
		while(!Thread.interrupted()) {
			event = (Event) events.removeFirst();
			
			if (event.getType() == EventType.FLOOR_BUTTON) {
				// send to scheduler after setting buttons of that
				fbEvent = (FloorButtonPressEvent) event;
				
				notPressed = floors[fbEvent.getFloor() - 1].requestDirection(fbEvent);
				
				// if notPressed is false that means button was already clicked (no need to request an elevator)
				if (notPressed) {
					schedulerEvents.addLast(fbEvent);
				}
			} else if (event.getType() == EventType.ELEVATOR_ARRIVED) {
				eaEvent = (ElevatorArriveEvent) event;
				elevatorButtons = floors[eaEvent.getFloor()-1].elevatorArrived(eaEvent.getDirection());
				
				reply = new ElevatorButtonPressEvent(elevatorButtons, eaEvent.getCar());
		
				schedulerEvents.addLast(reply);
			}
			
		}
	}
	
	public static void main(String[] args) {
		Thread f = new Thread(new FloorSubsystem("Test.txt", Configuration.NUM_FLOORS, new BoundedBuffer(), new BoundedBuffer()));
		f.start();
	}
}