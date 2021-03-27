package test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

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
@TestInstance(Lifecycle.PER_CLASS)
public class FloorSubsystemTest {

    private FloorSubsystem floorSubsystem;
    private Floor floor;

    @BeforeAll
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