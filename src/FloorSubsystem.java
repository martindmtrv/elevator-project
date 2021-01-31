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
	
	private Thread[] floors;
	private BoundedBuffer[] buffers;
	
	/**
	 * Create a new FloorSubsystem 
	 * @param fp - path to input file
	 * @param n - number of floors
	 */
	FloorSubsystem(String fp, int n) {
		inputFile = fp;
		floors = new Thread[n];
		buffers = new BoundedBuffer[n];
		
		// create all the floors and buffers
		for (int x = 0; x < n; x++) {
			buffers[x] = new BoundedBuffer();
			floors[x] = new Thread(new Floor(x+1, buffers[x]));
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
			return lines;
		} catch (FileNotFoundException e) {
			System.out.println(String.format("FLOORSUBSYSTEM: File %s not found", fp));
			return null;
		}
	}
	
	/**
	 * the floor subsystem will receive inputs from floors and pass them to the scheduler
	 * Need to use the bufferbox class later to allow many inputs to be received at once
	 * 
	 * For iteration 1 it will be read from a file
	 */
	
	@Override
	public void run() {
		// parse input
		ArrayList<String> file = readInput(inputFile);
		
		if (file == null) {
			return;
		}
		
		// start all the floors
		for (Thread t: floors) {
			t.start();
		}
		
		// add inputs to floor buffers
		for (String s: file) {
			String[] data = s.split(" ");
			int floor = Integer.parseInt(data[1]) - 1;
			
			buffers[floor].addLast(s);
		}
		
		// end the program
		for (Thread t: floors) {
			t.interrupt();
		}
	}
	
	public static void main(String[] args) {
		Thread f = new Thread(new FloorSubsystem("Test.txt", 10));
		
		f.start();
		try {
			f.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}