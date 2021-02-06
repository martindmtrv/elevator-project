import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases using JUnit5 framework to ensure the elevatorsubsystem is configured properly
 * and the appropriate states within the elevator are changed upon travelling to different floors.
 *
 * @Author: Alex Cameron
 */
class ElevatorSubsystemTest {
    private static ElevatorSubsystem elevatorSubsystem;
    private Elevator elevator;
    private String[] elevatorOutput;

    @BeforeEach
    public void setUp() {
        elevatorSubsystem = new ElevatorSubsystem(Configuration.NUM_CARS, Configuration.NUM_FLOORS,Configuration.INIT_CAR_FLOOR ,new BoundedBuffer(), new BoundedBuffer());
    }

    @Test
    @DisplayName("Test if elevator subsystem is created")
    public void testCreateElevatorSubsystem(){
        assertNotEquals(elevatorSubsystem,null);
    }

    @Test
    @DisplayName("Test number of floors in subsystem match number of floors in Config file")
    public void testElevatorConfig(){
        //Get number of cars in elevator subsystem
        Elevator[] elevatorCars = elevatorSubsystem.getElevators();
        //ensure car contains right number of floors as configuration file
        assertEquals(elevatorCars[Configuration.NUM_CARS-1].getNumFloors(),Configuration.NUM_FLOORS);
        assertEquals(elevatorCars.length,Configuration.NUM_CARS);
    }

    @Test
    @DisplayName("Test elevator enables and disables appropriate properties upon travelling to destination")
    public void testElevatorEvents(){
        //initialize a new elevator with ID: 100, 10 floors and current floor @ floor 1
        elevator = new Elevator(100,10,1);
        int destinationFloor = 4;
        // ELEVATOR departation expected properties
        String elevatorButton = "ELEVATORBUTTON: button 4 is pressed: true";
        String elevatorLamp = "ELEVATORLAMP: lamp 4 is lit: true";
        String elevatorMotor = "ELEVATORMOTOR: starts running";

        assertEquals(elevator.getCurrFloor(), 1); //ensure car starts at floor 1
        //track output from sequence of events elevator executes upon travelling from floor 1=>4
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        elevator.visitFloor(destinationFloor); //Floor 1 => 4

        //changes system.setout => system.out
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        elevatorOutput = (outContent.toString().split("\\r?\\n"));

        //TESTS BEFORE departure (before the elevator moves)
        int allConditionsSatisfied = 0;
        for(String s : elevatorOutput){
            if(s.contains(elevatorButton) || s.contains(elevatorLamp) || s.contains(elevatorMotor)){
                allConditionsSatisfied++;
            }
        }
        assertEquals(allConditionsSatisfied,3); //ensure elevator lamp, motor and button are correct states

        //TESTS AFTER arrival at destination floor
        //condition to determine that elevator reaches destination
        String elevatorArrivesDestination = "Car 100 moving UP, currently on floor: 4";
        boolean elevatorArrived = false;
        //new expected states upon arrival
        String elevatorDoor = "ELEVATORDOOR: is opening";
        elevatorButton = "ELEVATORBUTTON: button 4 is pressed: false";
        elevatorLamp = "ELEVATORLAMP: lamp 4 is lit: false";
        elevatorMotor = "ELEVATORMOTOR: stops running";
        allConditionsSatisfied = 0;

        for(String s : elevatorOutput){
            if(s.contains(elevatorArrivesDestination)){
                elevatorArrived = true;
            }
            //once elevator arrives check states
            if((s.contains(elevatorButton) || s.contains(elevatorLamp) || s.contains(elevatorMotor) || s.contains(elevatorDoor)) && elevatorArrived){
                allConditionsSatisfied++;
            }
        }
        assertEquals(allConditionsSatisfied,4); //ensure elevator lamp, motor, door and button are correct states
        assertEquals(elevator.getCurrFloor(), 4); //ensure elevator travels to floor 4

    }
}