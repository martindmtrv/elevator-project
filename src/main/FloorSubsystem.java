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
	
	/**
	 * Create a new FloorSubsystem 
	 * @param fp - path to input file
	 * @param n - number of floors
	 */
	FloorSubsystem(String fp, int n) {
		inputFile = fp;
		floors = new Floor[n];
		
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
			// TODO: with the data obj
			
			// send to appropriate floor
			// floors[x].requestDown() or floors[x].requestUp()
			
			
			// send to the scheduler
			System.out.println(String.format("Floor System: send to scheduler %s",s));
			
			// TODO: send to scheduler
			
			// TODO: await response from scheduler
			System.out.println("Floor System: waiting for response from scheduler");
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Floor System: do something with response");
		}	
	}
	
	public static void main(String[] args) {
		Thread f = new Thread(new FloorSubsystem("Test.txt", Configuration.NUM_FLOORS));
		
		f.start();
	}
}