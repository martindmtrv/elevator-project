package test;

import java.util.Arrays;

import event.DirectionType;
import event.Event;
import event.FloorButtonPressEvent;
import rpc.SerializationUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class SerializationTest {
	Event deserialized;
	
	@BeforeEach
	/**
	 * Serialize data and deserialize it to test if the class works
	 */
	public void SerializeAndDeserialize() {
		FloorButtonPressEvent event = new FloorButtonPressEvent("", 0, 5, DirectionType.UP);
		byte[] data = SerializationUtils.convertToBytes(event);
		this.deserialized = SerializationUtils.convertFromBytes(data);
	}
	
	@Test
	@DisplayName("Test once deserialized if the destination is correct")
	public void getDestinationData() {
		assertEquals(((FloorButtonPressEvent) deserialized).getDestination(), 5);
	}
	
	@Test
	@DisplayName("Test once deserialized if the direction is correct")
	public void getDirectionData() {
		assertEquals(((FloorButtonPressEvent) deserialized).getDirection(), DirectionType.UP);
	}
}
