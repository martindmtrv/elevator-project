import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FloorSubsystemTest {

    private FloorSubsystem floorSubsystem;
    //Test data in input Test.txt file
    private final String inputData[] = {"14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 7 Down 2","14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 7 Down 2","14:05:15.0 2 Up 4","14:15:30.0 3 Down 1","15:30:10.5 7 Down 2"} ;

    @BeforeEach
    void setUp() {
        floorSubsystem = new FloorSubsystem("Test.txt",10);
    }

    @Test
    public void testCreateFloorSubsystem(){
        assertNotEquals(floorSubsystem,null);
    }

    @Test
    public void testReadInputFile(){
        ArrayList<String> fileInput = floorSubsystem.readInput("Test.txt");

        for(int i=0;i<fileInput.size();i++){
            assertEquals(fileInput.get(i),inputData[i]);
        }
    }

}