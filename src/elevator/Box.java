package elevator;

import event.ElevatorCallToMoveEvent;
import event.Event;

/**
 * The basic Box class to show mutual exclusion and 
 * condition synchronization.
 * 
 * @author Lynn Marshall 
 * @version 1.00
 */
public class Box
{
    private Event contents = null; // contents
    private boolean empty = true; // empty?
    
    /**
     * Puts an object in the box.  This method returns when
     * the object has been put into the box.
     * 
     * @param item The object to be put in the box.
     */
    public synchronized void put(Event item) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        contents = item;
        empty = false;
        notifyAll();
    }
    
    /**
     * Gets an object from the box.  This method returns once the
     * object has been removed from the box.
     * 
     * @return The object taken from the box.
     */
    public synchronized Event get() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        Event item = contents;
        contents = null;
        empty = true;
        notifyAll();
        return item;
    }

}
