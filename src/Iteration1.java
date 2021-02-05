public class Iteration1 {

    private final static String testFile = "Test.txt";

    public static void main(String[] args) {
        //Put threads together and run main
    	BoundedBuffer floorQueue = new BoundedBuffer();
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	BoundedBuffer elevatorQueue = new BoundedBuffer();
    	
        
        Thread floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");
        
        // scheduler needs a copy of all three queues
        Thread scheduler;
        Thread elevator;
        //scheduler = new Thread(new Scheduler(), "scheduler");
        //elevator = new Thread(new Elevator(),"elevator"));


        floor.start();
        while (true) {
        	System.out.println("Scheduler would receive " + (Event)schedulerQueue.removeFirst());
        }
        //scheduler.start();
        //elevator.start();
    }
}
