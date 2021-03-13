package test;
import org.junit.jupiter.api.*;

import floor.Floor;
import floor.FloorSubsystem;
import main.Configuration;
import scheduler.BoundedBuffer;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases using JUnit5 framework to ensure all floors are configured properly and ensure
 * the input .txt file with the appropriate data is collected properly.
 *
 * @Author: Alex Cameron, Erdem Yanikomeroglu
 */
public class FloorSubsystemTest {

    private FloorSubsystem floorSubsystem;
    private Floor floor;
    //Test data in input Test.txt file
    private final String[] inputData = {"14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 1 Up 2","14:15:30.0 5 Down 1","15:30:10.5 7 Down 2","14:15:30.0 9 Down 1","15:30:10.5 6 Down 2","14:15:30.0 5 Down 4","15:30:10.5 6 Up 7","14:15:30.0 4 Down 2","15:30:10.5 3 Up 6"} ;
    private final String testFile = "Test.txt";

    @BeforeEach
    public void setUp() {
        floorSubsystem = new FloorSubsystem(Configuration.NUM_FLOORS, new BoundedBuffer(), new BoundedBuffer());
    }

    @Test
    @DisplayName("Test if floor subsystem is created")
    public void testCreateFloorSubsystem(){
        assertNotEquals(floorSubsystem,null);
    }

    @Test
    @DisplayName("Test number of floors in subsystem match number of floors in Config file")
    public void testFloorConfig(){
        assertEquals(floorSubsystem.getNumFloors(),Configuration.NUM_FLOORS);
    }

    @Test
    @DisplayName("First floor cannot request down")
    public void testFirstFloor(){
        int floorNum = 4;
        floor = new Floor(Configuration.NUM_FLOORS - (Configuration.NUM_FLOORS -1));
        assertNull(floor.getDownLamp()); //first floor should not be able to request down
        floor.requestUp(floorNum);
        assertTrue(floor.getUpLamp().getIsLit()); //up lamp enabled
        assertTrue(floor.getUpButton().getIsPressed()); //up button is pressed
    }

    @Test
    @DisplayName("Last floor cannot request up")
    public void testLastFloor(){
        int floorNum = 5;
        floor = new Floor(Configuration.NUM_FLOORS);
        assertNull(floor.getUpLamp()); //last floor should not be able to request up
        floor.requestDown(floorNum);
        assertTrue(floor.getDownLamp().getIsLit()); //down lamp enabled
        assertTrue(floor.getDownLamp().getIsLit()); //down button is pressed
    }

}