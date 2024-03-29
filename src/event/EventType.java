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
	ELEVATOR_TRIP_UPDATE, //it2
	ELEVATOR_APPROACH_SENSOR, //it2
	FAULT, //it4
	ELEVATOR_FAULT_UPDATE, //it4
	ELEVATOR_STOP,
	ELEVATOR_TRAVEL_TIMEOUT,
	NOTIFICATION
}
