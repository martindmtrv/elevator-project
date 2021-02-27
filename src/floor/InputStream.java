package floor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import event.DirectionType;
import event.FloorButtonPressEvent;
import scheduler.BoundedBuffer;
/**
 * This file reads requests from a text file and puts it into the floor subsystems queue
 * @author David Casciano
 *
 */
public class InputStream implements Runnable{
	private long diffInMillies;
	private String inputFile;
	private BoundedBuffer events;
	public InputStream(String fp, BoundedBuffer myQueue) {
		inputFile = fp;
		events = myQueue;
	}
	
	/**
	 * Read the input file in
	 * @param fp - the file path of the input file
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
		
		// do not use input file for time (use current time)
		return new FloorButtonPressEvent("", Integer.parseInt(input[1]), 
				Integer.parseInt(input[3]), DirectionType.valueOf(input[2].toUpperCase()));
		
	}
	@Override
	public void run() {
		//parse input
		ArrayList<String> file = readInput(inputFile);
		
		if (file == null) {
			return;
		}
		
		// add inputs to floor buffers
		for (int i = 0; i < file.size(); i++) {
			if(i+1 != file.size()) {
				String currString = file.get(i);
				String nextString = file.get(i+1);
				
				String[] curr;
				String[] next;
				
				curr = currString.split(" ");
				next = nextString.split(" ");
				
				Date t1, t2;
				try {
					t1 = new SimpleDateFormat("HH:mm:ss").parse(curr[0]);
					t2 = new SimpleDateFormat("HH:mm:ss").parse(next[0]);
					diffInMillies = t2.getTime() - t1.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				
			} else {
				diffInMillies = 0;
			}
			
			
			
			// parse data
			// add to FloorSubsystem event queue
			events.addLast(parseLine(file.get(i)));
			
			//Hard code time between inputs
			try {
				Thread.sleep(diffInMillies);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
