package rpc;

import scheduler.BoundedBuffer;
/**
 * Handler for Rpc activities. Handles all the heavy lifting for UDP stuff to make the rest of the
 * program unchanged.
 * @author Martin Dimitrov
 *
 */
public class RpcHandler implements Runnable {
	private Thread[] workers;
	
	/**
	 * Create an RpcHandler to hook up to the boundedbuffer input streams
	 * @param in - the bounded buffer to receive events into
	 * @param out - the boundedbuffer(s) to send out events from
	 * @param pSend - the port(s) to send out events too
	 * @param pReceive - the port(s) to listen for events from
	 */
	public RpcHandler(BoundedBuffer in, BoundedBuffer[] out, int[] pSend, int[] pReceive) {
		workers = new Thread[out.length * 2];
		int c = 0;
		
		// create all the worker threads 2 for each out
		for (int x=0; x < workers.length; x+=2) {
			workers[x] = new Thread(new RpcWorker(in, out[c], RpcWorkerType.SENDER, pSend[c]));
			workers[x+1] = new Thread(new RpcWorker(in, out[c], RpcWorkerType.RECEIVER, pReceive[c]));
			
			c++;
		}
	}

	@Override
	public void run() {
		// start the workers
		for (Thread t: workers) {
			t.start();
		}
	}
	
}
