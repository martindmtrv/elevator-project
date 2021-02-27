package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import main.Configuration;
import scheduler.BoundedBuffer;
import scheduler.Scheduler;
import floor.InputStream;

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

import elevator.Box;
class StateMachineTest {

    private final static String testFile = "TestComm.txt";
    private Thread floor, elevator, scheduler;
    private String[] threadOutput;

    public void setUpThreads(){
        //Put threads together and run main
        BoundedBuffer floorQueue = new BoundedBuffer();
        BoundedBuffer schedulerQueue = new BoundedBuffer();
        BoundedBuffer elevatorQueue = new BoundedBuffer();
        
        Configuration.VERBOSE = true;

        // floor get scheduler and floor queues
        floor = new Thread(new FloorSubsystem(Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");

        // elevator gets scheduler and elevator queues
        elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue, new Box()), "elevator");

        // scheduler needs a copy of all three queues
        scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));
        
        // inputstream
        Thread inputstream = new Thread(new InputStream(testFile, floorQueue));

        elevator.start();
        floor.start();
        inputstream.start();
        scheduler.start();
    }

    @BeforeEach
    public void getCommunicationData()throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        setUpThreads();

        //stop execution of threads
        Thread.sleep(30000);
        elevator.interrupt();
        floor.interrupt();
        scheduler.interrupt();

        //reset to system out (changes system.setout => system.out)
        //System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        threadOutput = (outContent.toString().split("\\r?\\n"));
    }
    
    @Test
    @DisplayName("Test Elevator Job State Machine")
    public void ElevatorJobStateTest() {
    	String initMsg = "ELEVATOR: Car 0 Job State Initialized to IDLE";
    	String idleMsg = "ELEVATOR: Car 0 Job State: IDLE->PICKING_UP";
    	String pickingupMsg = "ELEVATOR: Car 0 Job State: PICKING_UP->EN_ROUTE";
    	String enrouteMsg = "ELEVATOR: Car 0 Job State: EN_ROUTE->IDLE";
    	
    	int initLine = -1;
    	int idleLine = -1;
    	int pickingupLine = -1;
    	int enrouteLine = -1;
    	
    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(initMsg)){
                //test if the state machine properly initializes
                initLine = i;
                break;
            } 
        }
    	for(int i=initLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(idleMsg)){
                //test if the state has changed from Idle to picking up
                idleLine = i;
                break;
            }
        }
    	for(int i=idleLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(pickingupMsg)){
                //test if the state has changed from picking up to en route
                pickingupLine = i;
                break;
            }
        }
    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(enrouteMsg)){
                //if the state machine has transitioned through the states assert that it happened in the correct order
                enrouteLine = i;
                assertTrue((enrouteLine > pickingupLine) && (pickingupLine > idleLine) && (idleLine > initLine) && (initLine >= 0));
                break;
            }
        }
    }
    
    @Test
    @DisplayName("Test Elevator State Machine While The Elevator Traversing Upwards")
    public void ElevatorStateTest_UP() {
    	String initMsg = "ELEVATOR: Car 0 Initialized to IDLE";
    	String embarkMsg = "ELEVATOR: Car 0 IDLE->MOVING_UP";
    	String arriveMsg = "ELEVATOR: Car 0 MOVING_UP->IDLE";
    	String loadMsg = "ELEVATOR: Car 0 IDLE->LOADING_PASSENGER";
    	
    	int initLine = -1;
    	int embarkLine = -1;
    	int arriveLine = -1;
    	int loadLine = -1;
    	
    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(initMsg)){
                //test that the state was initialized correctly
                initLine = i;
                break;
            }
        }
    	for(int i=initLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(embarkMsg)){
                //test that the state properly changed while embarking
            	embarkLine = i;
                break;
            }
        }
    	for(int i=embarkLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(arriveMsg)){
                //test that the states properly changed while arriving
            	arriveLine = i;
                break;
            }
        }
    	for(int i=arriveLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(loadMsg)){
                //if the state machine has transitioned through the states assert that it happened in the correct order
            	loadLine = i;
                assertTrue((loadLine > arriveLine) && (arriveLine > embarkLine) && (embarkLine > initLine) && (initLine >= 0));
                break;
            }
        }
    }
    @Test
    @DisplayName("Test Elevator State Machine While The Elevator Traversing Downwards")
    public void ElevatorStateTest_DOWN() {
    	String initMsg = "ELEVATOR: Car 0 Initialized to IDLE";
    	String embarkMsg = "ELEVATOR: Car 0 IDLE->MOVING_DOWN";
    	String arriveMsg = "ELEVATOR: Car 0 MOVING_DOWN->IDLE";
    	String loadMsg = "ELEVATOR: Car 0 IDLE->LOADING_PASSENGER";
    	
    	int initLine = -1;
    	int embarkLine = -1;
    	int arriveLine = -1;
    	int loadLine = -1;
    	

    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(initMsg)){
            	//test that the state was initialized correctly
                initLine = i;
                break;
            }
        }
    	for(int i=initLine; i<threadOutput.length; i++){
    		
            if(threadOutput[i].contains(embarkMsg)){
            	 //test that the state properly changed while embarking
            	embarkLine = i;
                break;
            }
        }
    	for(int i=embarkLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(arriveMsg)){
            	//test that the states properly changed while arriving
            	arriveLine = i;
                break;
            }
        }
    	for(int i=arriveLine; i<threadOutput.length; i++){
            if(threadOutput[i].contains(loadMsg) && (i > arriveLine)){
            	//if the state machine has transitioned through the states assert that it happened in the correct order
            	loadLine = i;
                assertTrue((loadLine > arriveLine) && (arriveLine > embarkLine) && (embarkLine > initLine) && (initLine >= 0));
                break;
            }
        }
    }
    @Test
    @DisplayName("Test Scheduler State Machine")
    public void SchedulerStateTest() {
    	String initMsg = "SCHEDULER: Initialize state to WAITING";
    	String chg1 = "SCHEDULER: state change WAITING ->HANDLING";
    	String chg2 = "SCHEDULER: state change HANDLING ->WAITING";
    	
    	int initLine = -1;
    	int chg1Line = -1;
    	int chg2Line = -1;
    	
    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(initMsg)){
                //test if the state machine properly initializes
                initLine = i;
                break;
            } 
        }
    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(chg1)){
                //test if the state has changed from Idle to picking up
                chg1Line = i;
                break;
            }
        }
    	for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(chg2)){
                //if the state machine has transitioned through the states assert that it happened in the correct order
                chg2Line = i;
                assertTrue((chg2Line > chg1Line) && (chg1Line > initLine) && (initLine >= 0));
                break;
            }
        }
    }
}
