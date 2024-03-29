package event;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generic Event class subclassed by proper events
 * @author Martin Dimitrov
 */
public class Event implements Serializable {
	
	// to support serialization
	private static final long serialVersionUID = 5034032719498221414L;
	private String requestTime;
	private EventType type;
	private boolean eventSeen = false;
	
	/**
	 * Create a generic event (should not really be called since generic events are unused)
	 * Houses logic for creating timestamps used by subclasses
	 * @param rt - string representing request time (just use "" to have it generated based on current time)
	 * @param t - EventType to describe this event
	 */
	public Event(String rt, EventType t) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		
		// if no time specified use current time
		if (rt.equals("")) {
			requestTime = dateFormat.format(date);
		} else {
			requestTime = rt;
		}
		
		type = t;
	}
	
	public String toString() {
		return String.format("%s %s", type, requestTime);
	}

	public EventType getType() {
		return type;
	}
	
	/**
	 * Gets and formats the current time
	 * @return string representing current time
	 */
	public static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		
		return dateFormat.format(date);
	}

	/**
	 * Gets and formats the current time
	 * @return string representing current time
	 */
	public static Date getCurrTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		return date;
	}
	
	public void setSeen() {
		this.eventSeen = true;
	}
	
	public boolean getSeen() {
		return this.eventSeen;
	}
}
