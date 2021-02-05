import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Martin Dimitrov
 *
 */
public class Event {
	private String requestTime;
	private EventType type;
	
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
	
	public String getRequestTime() {
		return requestTime;
	}

	public EventType getType() {
		return type;
	}
}
