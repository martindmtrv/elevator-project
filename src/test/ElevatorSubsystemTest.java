package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import elevator.ElevatorSubsystem;
import elevator.Box;
import elevator.Elevator;
import floor.FloorSubsystem;
import floor.InputStream;
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

import elevator.Box;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ElevatorSubsystemTest {
	
	private static ElevatorSubsystem elevatorSubsystem;
    private final static String testFile = "TestComm.txt";
    private Thread floor, elevator, scheduler;
    private String[] threadOutput;

    public void setUpThreads(){
        //Put threads together and run main
        BoundedBuffer floorQueue = new BoundedBuffer();
        BoundedBuffer schedulerQueue = new BoundedBuffer();
        BoundedBuffer elevatorQueue = new BoundedBuffer();


        // floor get scheduler and floor queues
        floor = new Thread(new FloorSubsystem(Configuration.NUM_FLOORS, floorQueue, schedulerQueue), "floor");

        // elevator gets scheulder and elevator queues
        elevator = new Thread(new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR, elevatorQueue, schedulerQueue), "elevator");

        // scheduler needs a copy of all three queues
        scheduler = new Thread(new Scheduler(schedulerQueue, elevatorQueue, floorQueue));
        
        //inputstream
        
        Thread inputstream = new Thread(new InputStream(testFile, floorQueue));

        elevator.start();
        floor.start();
        inputstream.start();
        scheduler.start();
    }
    @BeforeAll
    @Test
    @DisplayName("4. Test if elevator subsystem is created")
    public void testSubsystemConstructor() {
    	elevatorSubsystem = new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR ,new BoundedBuffer(), new BoundedBuffer());
    	assertNotEquals(elevatorSubsystem,null);
    }
    
    @BeforeAll
    @Test
    @DisplayName("5. Test number of floors in subsystem match number of floors in Config file")
    public void testElevatorConstructor() {
    	elevatorSubsystem = new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR ,new BoundedBuffer(), new BoundedBuffer());
    	//Get number of cars in elevator subsystem
        Elevator[] elevatorCars = elevatorSubsystem.getElevators();
        //ensure car contains right number of floors as configuration file
        assertEquals(elevatorCars[Configuration.NUM_CARS-1].getNumFloors(),Configuration.NUM_FLOORS);
        assertEquals(elevatorCars.length,Configuration.NUM_CARS);
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
     * 
     * Test if elevator has moved
     * 
     */
    @Test
    @DisplayName("1. Test elevatorSubsystems ability to traverse floors")
    public void testElevatorTraversal() {
        int buttonPressLine =-1;
        int arrivalSensorLine = -1;

        String buttonPress = "FLOOR 2: 4 Added to floor destinations UP";
        String arrivalSensor = "SCHEDULER: elevator 0 is approaching floor 4 going UP";
        String arrivalNotification = "ELEVATOR: Car 0 has arrived floor 4";
        for(int i=0; i<threadOutput.length ;i++){
            //System.out.println(threadOutput[i]); //testing purposes
            if(threadOutput[i].contains(buttonPress)){
                buttonPressLine =i;
            }
        }
        for(int i=0; i<threadOutput.length; i++) {
        	if(threadOutput[i].contains(arrivalSensor)) {
        		arrivalSensorLine = i;
        	}
        }
        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(arrivalNotification)){
                //test that scheduler sends floor button event to elevator after button is pressed
                assertTrue(i>arrivalSensorLine && arrivalSensorLine >=0 && buttonPressLine >=0);
            }
        }
    }
    
    @Test
    @DisplayName("2. Test Elevator Components Behaviour On Departure")
    public void testElevatorDeparture() {
    	int doorLine = -1;
    	int btnLine = -1;
    	int lampLine = -1;
    	
    	String doorNotification = "ELEVATORDOOR: is closing";
    	String btnNotification = "ELEVATORBUTTON: button 4 is pressed: true";
    	String lampNotification = "ELEVATORLAMP: lamp 4 is lit: true";
    	String motorNotification = "ELEVATORMOTOR: starts running to move UP";
    	for(int i=0; i<threadOutput.length ;i++){
            //System.out.println(threadOutput[i]); //testing purposes
            if(threadOutput[i].contains(btnNotification)){
            	btnLine =i;
            }
        }
        for(int i=0; i<threadOutput.length; i++) {
        	if(threadOutput[i].contains(lampNotification)) {
        		lampLine = i;
        	}
        }
        for(int i=0; i<threadOutput.length; i++) {
        	if(threadOutput[i].contains(doorNotification)) {
        		doorLine = i;
        	}
        }
        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(motorNotification) && (i > doorLine)){
                //test that scheduler sends floor button event to elevator after button is pressed
                assertTrue(i>= 0 && lampLine >= 0 && btnLine >=0 && doorLine >=0);
            }
        }
    	
    }
    @Test
    @DisplayName("3. Test Elevator Components Behaviour On Arrival")
    public void testElevatorArrival() {
    	int doorLine = -1;
    	int motorLine = -1;
    	int lampLine = -1;
    	
    	String motorNotification = "ELEVATORMOTOR: stops running";
    	String doorNotification = "ELEVATORDOOR: is opening";
    	String lampNotification = "ELEVATORLAMP: lamp 1 is lit: false";
    	String btnNotification = "ELEVATORBUTTON: button 1 is pressed: false";
    	for(int i=0; i<threadOutput.length ;i++){
            //System.out.println(threadOutput[i]); //testing purposes
            if(threadOutput[i].contains(motorNotification)){
            	motorLine =i;
            }
        }
        for(int i=0; i<threadOutput.length; i++) {
        	if(threadOutput[i].contains(doorNotification)) {
        		doorLine = i;
        	}
        }
        for(int i=0; i<threadOutput.length; i++) {
        	if(threadOutput[i].contains(lampNotification)) {
        		lampLine = i;
        	}
        }
        for(int i=0; i<threadOutput.length; i++){
            if(threadOutput[i].contains(btnNotification)){
                //test that scheduler sends floor button event to elevator after button is pressed
                assertTrue(i>lampLine && lampLine >= 0 && motorLine >=0 && doorLine >=0);
            }
        }
    }
   
}