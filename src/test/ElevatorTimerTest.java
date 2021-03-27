package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import event.ElevatorTripUpdateEvent;
import main.Configuration;
import scheduler.BoundedBuffer;
import scheduler.ElevatorEventTimer;
import scheduler.ElevatorTripUpdate;

/**
 * Test for the elevator timeouts
 * @author Martin Dimitrov
 *
 */
public class ElevatorTimerTest {
	
	/**
     * Test Elevator Event timeout
     * When telling elevator to stop we should here back within 
     * (Configuration.TRAVEL_TIME_BETWEEN_FLOOR / 2 + Configuration.TIMEOUT_DELAY) ms
     */
    @Test
    @DisplayName("1. Test ElevatorEventTimer timeout")
    public void testElevatorTimeout() {
    	
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	ElevatorTripUpdateEvent test = new ElevatorTripUpdateEvent(0, 1, ElevatorTripUpdate.STOP);
    	
    	Thread timer = new Thread(new ElevatorEventTimer(test, schedulerQueue, 0));
    	timer.start();
    	
    	// sleep and make sure timer passes the timeout event
    	try {
			Thread.sleep(Configuration.TRAVEL_TIME_BETWEEN_FLOOR / 2 + Configuration.TIMEOUT_DELAY + 1000);
		} catch (InterruptedException e) {
			return;
		}
    	
    	assertEquals(1, schedulerQueue.size());
    }
    
    /**
     * Test Elevator Event timer cancel
     * Canceling timer should not send the timeout event
     */
    @Test
    @DisplayName("2. Test ElevatorEventTimer timer stop")
    public void testElevatorTimerCancel() {
    	
    	BoundedBuffer schedulerQueue = new BoundedBuffer();
    	ElevatorTripUpdateEvent test = new ElevatorTripUpdateEvent(0, 1, ElevatorTripUpdate.STOP);
    	
    	Thread timer = new Thread(new ElevatorEventTimer(test, schedulerQueue, 0));
    	timer.start();
    	
    	// sleep and make sure timer passes the timeout event
    	try {
			Thread.sleep(Configuration.TRAVEL_TIME_BETWEEN_FLOOR / 2);
		} catch (InterruptedException e) {
			return;
		}
    	
    	// stop the timer early
    	timer.interrupt();
    	
    	// wait a bit to make sure timer doesn't send event
    	try {
			Thread.sleep(Configuration.TIMEOUT_DELAY + 1000);
		} catch (InterruptedException e) {
			return;
		}
    	
    	assertEquals(0, schedulerQueue.size());
    }

}
