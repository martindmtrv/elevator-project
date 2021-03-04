package rpc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import event.Event;
import scheduler.BoundedBuffer;

public class RpcWorker implements Runnable {
	private BoundedBuffer inQueue;
	private BoundedBuffer outQueue;
	private int port;
	
	private RpcWorkerType type;
	private DatagramSocket socket;
	
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
		}
		
		
	}
	
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
	
	public void rpc_receive(DatagramPacket receive) {
		// receive then send
		RpcWorker.receivePacket(socket, receive);
		DatagramPacket ack = new DatagramPacket(new byte[1], 1, receive.getAddress(), receive.getPort());
		RpcWorker.sendPacket(socket, ack);
		
	}
	
	public static void receivePacket(DatagramSocket socket, DatagramPacket packet) {
		try {
		   socket.receive(packet);
		} catch (IOException e) {
		   e.printStackTrace();
		   System.exit(1);
		}
	}
	
	public static void sendPacket(DatagramSocket socket, DatagramPacket packet) {
		try {
		   socket.send(packet);
		} catch (IOException e) {
		   e.printStackTrace();
		   System.exit(1);
		}
	}
	
	public void handleIncoming() {
		Event event;
		byte[] data;
		byte[] truncatedData;
		DatagramPacket incoming;
		
		while (!Thread.interrupted()) {
			// create a buffer
			data = new byte[1000];
			
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
			try {
				// TODO: put this in configuration
				outgoing = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
			} catch (UnknownHostException e) {
				System.out.println(e);
				return;
			}
			
			rpc_send(outgoing);
			
		}
	}

	@Override
	public void run() {
		if (type == RpcWorkerType.RECEIVER) {
			handleIncoming();
		} else {
			handleOutgoing();
		}
	}
}
