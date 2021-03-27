package test;


import event.DirectionType;
import event.Event;
import event.FloorButtonPressEvent;
import rpc.SerializationUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import floor.Floor;

@TestInstance(Lifecycle.PER_CLASS)
public class SerializationTest {
	Event deserialized;
	
	
	
	/**
	 * Serialize data and deserialize it to test if the class works
	 */
	@BeforeAll
	public void SerializeAndDeserialize() {
		Floor state = new Floor(0);
		FloorButtonPressEvent event = new FloorButtonPressEvent("", 0, 5, DirectionType.UP);
		event.setState(state);
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
	@Test
	@DisplayName("Test once deserialized if the state is correct")
	public void getStateData() {
		assertEquals(((FloorButtonPressEvent) deserialized).getState().getFloorNum(), 0);
	}
}
