public class Iteration1 {

    private final static String testFile = "Test.txt";

    public static void main(String[] args) {
        //Put threads together and run main
        Thread elevator, floor, scheduler;
        floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS), "floor");
        //scheduler = new Thread(new Scheduler(), "scheduler");
        //elevator = new Thread(new Elevator(),"elevator"));


        floor.start();
        //scheduler.start();
        //elevator.start();
    }
}
