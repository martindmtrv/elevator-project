public class Iteration1 {

    private final static String testFile = "Test.txt";

    public static void main(String[] args) {
        //Put threads together and run main
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	
        
        Thread floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS, floorQueue, elevatorQueue), "floor");
        //Don't forget to change the floorQueue to schedulerQueue
        Thread elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, floorQueue), "elevator");
       
        // scheduler needs a copy of all three queues
        Thread scheduler;
        //scheduler = new Thread(new Scheduler(), "scheduler");

        elevator.start();
        floor.start();
        
       // while (true) {
        //	System.out.println("Scheduler would receive " + (Event)schedulerQueue.removeFirst());
        //}
        //scheduler.start();
    }
}
