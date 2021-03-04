package rpc;

import java.util.Arrays;

import event.DirectionType;
import event.Event;
import event.FloorButtonPressEvent;

public class RpcUtils {
	public static void main(String[] args) {
		FloorButtonPressEvent event = new FloorButtonPressEvent("", 0, 5, DirectionType.UP);
		
		
		System.out.println(event);
		
		byte[] data = SerializationUtils.convertToBytes(event);
		
		System.out.println(Arrays.toString(data));
		
		Event deserialized = SerializationUtils.convertFromBytes(data);
		
		System.out.println(deserialized);
		System.out.println(((FloorButtonPressEvent) deserialized ).getDestination());
		System.out.println(((FloorButtonPressEvent) deserialized ).getDirection());
		
	}
}
