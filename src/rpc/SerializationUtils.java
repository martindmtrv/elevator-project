package rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import event.Event;

/**
 * A helper class for Serializing and deserializing byte / Event data
 * @author Martin Dimitrov
 *
 */
public class SerializationUtils {
	/**
	 * A method for converting bytes into an Event object
	 * @param bytes - bytes received from UDP packet
	 * @return event - the event converted from bytes (or null if it failed to convert)
	 */
	public static Event convertFromBytes(byte[] bytes) {
		// create object stream to convert bytes to object
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		         	ObjectInputStream in = new ObjectInputStream(bis)) {
				Object readIn = in.readObject();
		        return (Event) readIn;
		} catch (Exception e) {
			
			System.out.println("RPCUtil: Could not convert bytes to Event");
			return null;
		}
	}
	
	/**
	 * Convert an event object into bytes in preparation to be sent via UDP
	 * @param event - the event to convert to bytes
	 * @return bytes - the serialized byte array
	 */
	public static byte[] convertToBytes(Event event) {
		// create byte stream to convert our object to bytes
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		         	ObjectOutputStream out = new ObjectOutputStream(bos)) {
		        out.writeObject(event);
		        return bos.toByteArray();
		} catch (Exception e) {
			System.out.println("RPCUtil: Could not convert Event to bytes");
			return null;
		}
	}
}
