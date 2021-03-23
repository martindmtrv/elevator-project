package main;
import java.net.InetAddress;

import elevator.Box;
import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import floor.InputStream;
import scheduler.BoundedBuffer;
import scheduler.Scheduler;

public class Project {
    public static void main(String[] args) {
        //Put threads together and run main
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
        
    	// floor get scheduler and floor queues
        Thread floor = new Thread(new FloorSubsystem(Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");
        
        // elevator gets scheduler and elevator queues
        Thread elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue), "elevator");
       
        // scheduler needs a copy of all three queues
        Thread scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));
        
        // InputStream gets the floors queue
        Thread inputstream = new Thread(new InputStream(Configuration.TEST_FILE, floorQueue));
        

        elevator.start();
        floor.start();
        inputstream.start();
        scheduler.start();
    }
}
