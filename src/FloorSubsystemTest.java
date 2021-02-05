import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

class FloorSubsystemTest {

    private FloorSubsystem floorSubsystem;
    //Test data in input Test.txt file
    private final String[] inputData = {"14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 7 Down 2","14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 7 Down 2","14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 7 Down 2"} ;
    private final String testFile = "Test.txt";

    @Before
    void setUp() {
        floorSubsystem = new FloorSubsystem(testFile,10);
    }

    @Test
    public void testCreateFloorSubsystem(){
        assertNotEquals(floorSubsystem,null);
    }

    @Test
    public void testReadInputFile(){
        File file = new File(testFile);
        assertTrue(file.exists());
        ArrayList<String> fileInput = floorSubsystem.readInput(testFile);

        for(int i=0;i<fileInput.size();i++){
            assertEquals(fileInput.get(i),inputData[i]);
        }
    }
}