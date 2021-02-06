package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import main.Configuration;
import scheduler.BoundedBuffer;
import scheduler.Scheduler;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Test cases using JUnit5 framework to ensure all events which include communication
 * between the ElevatorSubsystem, FloorSubsystem and Scheduler occur in an appropriate
 * sequential manner.
 *
 * @Author: Alex Cameron
 */
class Iteration1Test {

    private final static String testFile = "TestComm.txt";
    private Thread floor, elevator, scheduler;
    private String[] threadOutput;

    public void setUpThreads(){
        //Put threads together and run main
        BoundedBuffer floorQueue = new BoundedBuffer();
        BoundedBuffer schedulerQueue = new BoundedBuffer();
        BoundedBuffer elevatorQueue = new BoundedBuffer();


        // floor get scheduler and floor queues
        floor = new Thread(new FloorSubsystem(testFile, Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");

        // elevator gets scheulder and elevator queues
        elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue), "elevator");

        // scheduler needs a copy of all three queues
        scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));

        elevator.start();
        floor.start();
        scheduler.start();
    }

    @BeforeEach
    public void getCommunicationData()throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        setUpThreads();

        //stop execution of threads
        Thread.sleep(5000);
        elevator.interrupt();
        floor.interrupt();
        scheduler.interrupt();

        //reset to system out (changes system.setout => system.out)
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        threadOutput = (outContent.toString().split("\\r?\\n"));
    }

    /**
     * Testing for: EVENT: 14:15:30.0 3 Down 1 (text file input)
     *         Time of event:       14:15:30.0
     *         Starting Floor:      3
     *         Direction:           Down
     *         Destination Floor:   1
     */
    @Test
    @DisplayName("1. Test floorsubsystem sends data to scheduler @ EVENT: 14:15:30.0 3 Down 1")
    public void testButtonPressEvent() {
        int buttonPressLine=-1;

        String buttonPress = "FLOOR 2: UP Button Pressed";
        String destinationSent = "SCHEDULER: Sending event FLOOR_BUTTON";
        for(int i=0; i<threadOutput.length ;i++){
            //System.out.println(threadOutput[i]); //testing purposes
            if(threadOutput[i].contains(buttonPress)){
                assertEquals(buttonPress, threadOutput[i]);
                buttonPressLine =i;
            }
        }
        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(destinationSent)){
                //test that scheduler sends floor button event to elevator after button is pressed
                assertTrue(i>buttonPressLine && buttonPressLine>=0);
            }
        }
    }

    /**
     * Note: There is currently only one car running in Iteration1 called Car 0.
     */
    @Test
    @DisplayName("2. Test elevator receives event info from scheduler @ EVENT: 14:15:30.0 3 Down 1")
    public void testElevatorFloorArrivalEvent(){
        //Testing for: EVENT: 14:15:30.0 3 Down 1
        //Time of event: 14:15:30.0
        //Starting Floor: 3
        //Direction: 1
        int elevatorArrivalLine = -1;
        String elevatorArrival = "ELEVATOR: Car 0 has arrived floor 2";
        String elevatorDirection = "ELEVATOR: Car 0 moving UP, is currently on floor: 2";

        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(elevatorArrival)){
                //test that elevator arrives at floor 2 upon being notified by scheduler
                assertEquals(elevatorArrival,threadOutput[i]);
                elevatorArrivalLine = i;
            }
        }
        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(elevatorDirection)){
                //test that elevator arrives at floor 2 before moving in UP direction
                assertTrue(i>elevatorArrivalLine && elevatorArrivalLine>=0);
            }
        }
    }


    /**
     * Note: There is currently only one car running in Iteration1 called Car 0.
     */
    @Test
    @DisplayName("3. Test scheduler sends elevator arrival info to floor 2 and receives successfully @ EVENT: 14:15:30.0 3 Down 1")
    public void testSchedulerFloorArrivalEvent(){
        int schedulerArrivalEventLine = -1;
        String schedulerToFloor = "Sending event ELEVATOR_ARRIVED";
        String elevatorArrivalFloor = "FLOOR 2: Elevator arrived, going UP";
        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(schedulerToFloor)){
                //test that elevator arrives at floor 2 upon being notified by scheduler
                schedulerArrivalEventLine = i;
            }else if(threadOutput[i].contains(elevatorArrivalFloor)){
                assertTrue(i>schedulerArrivalEventLine && schedulerArrivalEventLine>-1);
            }
        }
    }

    /**
     * Note: There is currently only one car running in Iteration1 called Car 0.
     */
    @Test
    @DisplayName("4. Test sending destinations to scheduler and ensure elevator receives destination @ EVENT: 14:15:30.0 3 Down 1")
    public void testElevatorButtonEvent(){
        int sendSchedulerDestinationLine = -1;
        int sendElevatorDestinationLine = -1;
        String floorSubsystemSendDestination = "FLOORSUBSYSTEM: sending destinations [4]";
        String schedulerSendElevatorButtonEvent = "SCHEDULER: Sending event ELEVATOR_BUTTONS";
        String floorDestinationInfo = "[4]";
        String elevatorReceiveDestination = "ELEVATOR: Car 0 moving UP, is currently on floor: 2";

        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(floorSubsystemSendDestination)){
                //Line which floor subsystem sends destination to scheduler
                sendSchedulerDestinationLine = i;
            }else if(threadOutput[i].contains(schedulerSendElevatorButtonEvent) && threadOutput[i].contains(floorDestinationInfo) && sendSchedulerDestinationLine > -1){
                //ensure scheduler sends ELEVATOR_BUTTON event after floor subsystem sends scheduler destination info
                sendElevatorDestinationLine =i;
                assertTrue(i>sendSchedulerDestinationLine);
            }else if(threadOutput[i].contains(elevatorReceiveDestination) && sendElevatorDestinationLine > -1){
                assertTrue(i>sendElevatorDestinationLine);
            }
        }
    }

    /**
     *
     */
    @Test
    @DisplayName("5. Test Elevator arrives at destination and sends info from scheduler to floor")
    public void testElevatorReceiveButtonEvent() {
        int elevatorNotifyArrivalLine = -1;
        int schedulerNotifyArrivalLine = -1;
        String elevatorArriveDestination = "ELEVATOR: Car 0 moving UP, is currently on floor: 4";
        String schedulerNotifiesFloor = "SCHEDULER: Sending event ELEVATOR_ARRIVED";
        String floorReceivesArrival = "Elevator arrived, going UP";

        for (int i = 0; i < threadOutput.length; i++) {
            if (threadOutput[i].contains(elevatorArriveDestination)) {
                //Line which elevator arrives at the destination floor
                elevatorNotifyArrivalLine = i;
            } else if (threadOutput[i].contains(schedulerNotifiesFloor) && elevatorNotifyArrivalLine >-1) {
                //The scheduler has notified the floor of the elevator's arrival
                schedulerNotifyArrivalLine = i;
                assertTrue(i > elevatorNotifyArrivalLine);
            } else if (threadOutput[i].contains(floorReceivesArrival) && schedulerNotifyArrivalLine > -1) {
                assertTrue(i > schedulerNotifyArrivalLine);
            }
        }
    }
}