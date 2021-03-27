package test;

import event.Event;
import rpc.SerializationUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import event.Fault;
import event.FaultType;

@TestInstance(Lifecycle.PER_CLASS)
public class FaultEventTest {
	Fault fault;
	Event deserialized;
	
	/**
	 * Create a fault event
	 * Serialize data and deserialize it to test if the class works
	 */
	@BeforeAll
	public void createFault() {
		this.fault = new Fault(1, FaultType.DOOR_STUCK);
		byte[] data = SerializationUtils.convertToBytes(this.fault);
		this.deserialized = SerializationUtils.convertFromBytes(data);
	}
	

	/**
	 * 
	 */
	@Test
	@DisplayName("Floor ID is stored correctly")
	public void getDestinationData() {
		assertEquals(fault.getCar(), 1);
	}
	
	@Test
	@DisplayName("Fault Type is stored correctly")
	public void getDirectionData() {
		assertEquals(fault.getFaultType(), FaultType.DOOR_STUCK);
	}
	@Test
	@DisplayName("Check if it can be deserialized properly")
	public void getStateData() {
		assertEquals(((Fault) deserialized).getCar(), 1);
		assertEquals(((Fault) deserialized).getFaultType(), FaultType.DOOR_STUCK);
	}
}
