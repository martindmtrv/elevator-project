package rpc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import event.Event;
import main.Configuration;
import scheduler.BoundedBuffer;

/**
 * Rpc worker for the Handler class. Each one of these will be assigned a port to either send or receive to.
 * Each worker will be assigned a task depending on its type (whether it is sending or receiving events)
 * @author Martin Dimitrov
 *
 */
public class RpcWorker implements Runnable {
	private BoundedBuffer inQueue;
	private BoundedBuffer outQueue;
	private int port;
	
	private RpcWorkerType type;
	private DatagramSocket socket;
	
	// for outgoing workers only
	private InetAddress address;
	
	/**
	 * Create an RpcWorker
	 * @param in - boundedbuffer to pass in events to
	 * @param out - boundedbuffer to send events out
	 * @param t - type of worker
	 * @param p - port to listen/send (depending on type)
	 */
	public RpcWorker(BoundedBuffer in, BoundedBuffer out, RpcWorkerType t, int p) {
		inQueue = in;
		outQueue = out;
		port = p;
		type = t;
		
		if (type == RpcWorkerType.RECEIVER) {
			try {
		        socket = new DatagramSocket(port);
		    } catch (SocketException se) {   // Can't create the socket.
		        se.printStackTrace();
		        System.exit(1);
		    }
		} else {
			try {
				if (Configuration.USE_LOCALHOST) {
					address = InetAddress.getLocalHost();
				} else {
					if (port == Configuration.ELEVATOR_PORT) {
						address = InetAddress.getByName(Configuration.ELEVATOR_SYSTEM_ADDRESS);
					} else if (port == Configuration.FLOOR_PORT) {
						address = InetAddress.getByName(Configuration.FLOOR_SYSTEM_ADDRESS);
					} else if (port == Configuration.SCHEDULER_LISTEN_ELEVATOR_PORT || port == Configuration.SCHEDULER_LISTEN_FLOOR_PORT) {
						address = InetAddress.getByName(Configuration.SCHEDULER_SYSTEM_ADDRESS);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
				return;
			}
			
		}
		
		
	}
	
	/**
	 * Send an event and await an acknowledgement,
	 * timeouts will be handled here too in later iteration
	 * @param send - packet to send
	 */
	public void rpc_send(DatagramPacket send) {
		DatagramPacket receive = new DatagramPacket(new byte[1], 1);
		
		// make a new one every time to send stuff
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			return;
		}

		// send then receive
		RpcWorker.sendPacket(socket, send);
		RpcWorker.receivePacket(socket, receive); // receive just for acknowledgement
		
		// TODO: handle timeouts here (maybe depending on what type was sent)
		
		socket.close();
	}
	
	/**
	 * Receive an event and send off acknowledgement
	 * @param receive - packet to receive
	 */
	public void rpc_receive(DatagramPacket receive) {
		// receive then send
		RpcWorker.receivePacket(socket, receive);
		DatagramPacket ack = new DatagramPacket(new byte[1], 1, receive.getAddress(), receive.getPort());
		RpcWorker.sendPacket(socket, ack);
	}
	
	/**
	 * Reuseable method to receive a packet on a particular socket
	 * @param socket - socket to receive from
	 * @param packet - packet to send
	 */
	public static void receivePacket(DatagramSocket socket, DatagramPacket packet) {
		try {
		   socket.receive(packet);
		} catch (IOException e) {
		   e.printStackTrace();
		   System.exit(1);
		}
	}
	
	/**
	 * Reuseable method to send a packet on a particular socket
	 * @param socket - socket to send on
	 * @param packet - packet to receive
	 */
	public static void sendPacket(DatagramSocket socket, DatagramPacket packet) {
		try {
		   socket.send(packet);
		} catch (IOException e) {
		   e.printStackTrace();
		   System.exit(1);
		}
	}
	
	/**
	 * Function for handling incoming events
	 * this is what is handled by RpcWorkerType.RECEIVER
	 */
	public void handleIncoming() {
		
		Event event;
		byte[] data;
		byte[] truncatedData;
		DatagramPacket incoming;
		
		while (!Thread.interrupted()) {
			// create a buffer
			data = new byte[2000];
			
			// setup datagram packet
			incoming = new DatagramPacket(data, data.length);
			
			// receive something
			rpc_receive(incoming);
			
			// remove whitespace from end of the array
			truncatedData = Arrays.copyOfRange(data, 0, incoming.getLength());
			
			// serialize into an event then add it to the queue
			event = SerializationUtils.convertFromBytes(truncatedData);
			inQueue.addLast(event);
		}
	}
	
	/**
	 * Function for handling outgoing events
	 * this is what is handled by RpcWorkerType.SENDER
	 */
	public void handleOutgoing() {
		Event event;
		byte[] data;
		DatagramPacket outgoing;
		
		while (!Thread.interrupted()) {
			// grab an event to send
			event = (Event) outQueue.removeFirst();
			
			// serialize into bytes
			data = SerializationUtils.convertToBytes(event);
			
			// setup datagram packet
			outgoing = new DatagramPacket(data, data.length, address, port);
			
			rpc_send(outgoing);
			
		}
	}

	@Override
	public void run() {
		// run the correct handler
		if (type == RpcWorkerType.RECEIVER) {
			handleIncoming();
		} else {
			handleOutgoing();
		}
	}
}
