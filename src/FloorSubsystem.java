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
	
	/**
	 * Create a new FloorSubsystem 
	 * @param fp - path to input file
	 * @param n - number of floors
	 */
	FloorSubsystem(String fp, int n) {
		inputFile = fp;
		floors = new Floor[n];
		events = new BoundedBuffer();
		
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
	 * the floor subsystem will receive inputs from a file and pass them to the scheduler
	 */
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
			// add to my event queue
			events.addLast(new Request(s, RequestType.FLOOR_BUTTON));
		}
		
		// run until stopped
		while(!Thread.interrupted()) {
			Request event = (Request) events.removeFirst();
			
			if (event.getRequestType() == RequestType.FLOOR_BUTTON) {
				// send to scheduler after setting buttons of that
				floors[event.getFloor()].requestDirection(event.getDirection());
				
				// send to scheduler
			}
			
		}
	}
	
	public static void main(String[] args) {
		Thread f = new Thread(new FloorSubsystem("Test.txt", Configuration.NUM_FLOORS));
		
		f.start();
	}
}