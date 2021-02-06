public class Iteration1 {

    private final static String testFile = "Test.txt";

    public static void main(String[] args) {
        //Put threads together and run main
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	
        
    	// floor get scheduler and floor queues
        Thread floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");
        
        // elevator gets scheulder and elevator queues
        Thread elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue), "elevator");
       
        // scheduler needs a copy of all three queues
        Thread scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));

        elevator.start();
        floor.start();
        scheduler.start();
        
    }
}
