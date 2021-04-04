package scheduler;

public enum ElevatorJobState {
	PICKING_UP,
	IDLE,
	EN_ROUTE,
	FAULT,
	ARRIVAL_SENSOR_FAIL,
	MOTOR_FAIL,
	TIMED_OUT,
	DOOR_STUCK
}
