package main;

import java.io.File;
import java.io.IOException;

//unused imports here to make sure the classes we are running will get compiled
import elevator.ElevatorSubsystem;
import scheduler.Scheduler;
import floor.FloorSubsystem;

/**
 * Class setup to run the system as seperate processes, by spawning them from a single process (like fork())
 * Useful for running the system with UDP without needing to start 3 processes
 * @author Martin Dimitrov
 */
@SuppressWarnings("unused")
public class ProjectUDP {
	
	public static void main(String[] args) {
		// create the processes
		ProcessBuilder elevator = new ProcessBuilder("java", "-cp", "bin", "elevator.ElevatorSubsystem");
		ProcessBuilder floor = new ProcessBuilder("java","-cp", "bin", "floor.FloorSubsystem");
		ProcessBuilder scheduler = new ProcessBuilder("java", "-cp", "bin", "scheduler.Scheduler");
		
		// set their stdin, stderr, stdout to the same as this process
		// or to be a logfile
		if (Configuration.outputLogFiles) {
			File elOutput = new File("logs/elevatorLog.txt");
			File flOutput = new File("logs/floorLog.txt");
			File scOutput = new File("logs/schedulerLog.txt");
			
			// clear old logs
			elOutput.delete();
			flOutput.delete();
			scOutput.delete();
			
			// create the new logfiles
			try {
				elOutput.createNewFile();
				flOutput.createNewFile();
				scOutput.createNewFile();
			} catch(IOException e) {
				System.out.println("Unable to create logs" + e);
				return;
			}
			
			
			elevator.redirectOutput(elOutput).redirectError(elOutput);
			floor.redirectOutput(flOutput).redirectError(flOutput);
			scheduler.redirectOutput(scOutput).redirectError(scOutput);
			
		} else {
			elevator.inheritIO();
			floor.inheritIO();
			scheduler.inheritIO();
		}
		
		
		Process[] processes = new Process[3];
		
		// start them all
		try {
			processes[2] = elevator.start();
			processes[1] = scheduler.start();
			// sleep so the gui can get setup (loading...)
			try {
				Thread.sleep(5000);
			} catch(InterruptedException e) {
				return;
			}
			processes[0] = floor.start();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		// make sure the processes get killed after (otherwise they will be stuck on the OS)
		try {
			// hit x on GUI to close all processes
			processes[1].waitFor();
			System.out.println("Making sure all processes are killed");
			for (Process p: processes) {
				p.destroy();
			}
		} catch (InterruptedException e) {
			System.out.println("Making sure all processes are killed");
			for (Process p: processes) {
				p.destroy();
			}
		}
	}

}
