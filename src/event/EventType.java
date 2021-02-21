package event;

/**
 * Enum for all the Event types (will be used when sending bytes)
 * @author Martin Dimitrov
 */
public enum EventType {
	FLOOR_BUTTON,		//Requesting elevator from floor
	ELEVATOR_ARRIVED,	//
	ELEVATOR_BUTTONS,	//Requesting destination
	ELEVATOR_CALLED,	
	ELEVATOR_MOVING,
	ELEVATOR_ARRIVAL_SENSOR, //it2
	ELEVATOR_APPROACH_SENSOR //it2
}
