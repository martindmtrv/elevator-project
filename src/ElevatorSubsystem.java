import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Elevator Subsystem for the elevator project. Controls all the elevators and handles sending input to the scheduler
 * @author Ammar Tosun
 */
public class ElevatorSubsystem implements Runnable {
	
	private Thread[] elevators;

	/**
	 * Create a new ElevatorSubsystem 
	 * @param n - number of elevators
	 * @param f - number of floors
	 */
	public ElevatorSubsystem(int n, int f) {
		elevators = new Thread[n];
		
		// create all the elevator threads
		for (int x = 0; x < n; x++) {
			String eThreadName = "Elevator" + (x+1);
			elevators[x] = new Thread(new Elevator(eThreadName, f));
		}
	}

	
	/**
	 * The elevator subsystem will receive inputs from scheduler
	 * For iteration 1 it will be read from a file
	 */
	
	@Override
	public void run() {
		
		// start all the floors
		for (Thread t: elevators) {
			t.start();
		}

		
		// end the program
		for (Thread t: elevators) {
			t.interrupt();
		}
	}
	
	public static void main(String[] args) {
		Thread f = new Thread(new ElevatorSubsystem(1, 10));
		
		f.start();
		try {
			f.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
