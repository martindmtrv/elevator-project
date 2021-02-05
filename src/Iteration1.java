public class Iteration1 {

    private final static String testFile = "Test.txt";

    public static void main(String[] args) {
        //Put threads together and run main
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	
        
        Thread floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");
        Thread elevator = new Thread(new ElevatorSubsysem(Configuration.NUM_CARS, Configuration.NUM_FLOORS, elevatorQueue, schedulerQueue));
       
        // scheduler needs a copy of all three queues
        Thread scheduler;
        //scheduler = new Thread(new Scheduler(), "scheduler");


        floor.start();
        elevator.start();
        while (true) {
        	System.out.println("Scheduler would receive " + (Event)schedulerQueue.removeFirst());
        }
        //scheduler.start();
    }
}
