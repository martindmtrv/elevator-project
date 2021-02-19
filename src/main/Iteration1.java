package main;
import elevator.Box;
import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import scheduler.BoundedBuffer;
import scheduler.Scheduler;

public class Iteration1 {

    private final static String testFile = "TestComm.txt";

    public static void main(String[] args) {
        //Put threads together and run main
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	
        
    	// floor get scheduler and floor queues
        Thread floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");
        
        // elevator gets scheduler and elevator queues
        Thread elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue, new Box()), "elevator");
       
        // scheduler needs a copy of all three queues
        Thread scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));

        elevator.start();
        floor.start();
        scheduler.start();
        
    }
}
