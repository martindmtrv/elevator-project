package event;

import java.util.Arrays;

/**
 * Event to represent elevator presses after elevator arrives at a floor
 * @author Martin Dimitrov
 */
public class ElevatorButtonPressEvent extends Event {
	private Integer[] buttons;
	private int car;
	
	/**
	 * Create this event
	 * @param b - list of integers for the destinations
	 * @param c - car number
	 */
	public ElevatorButtonPressEvent(Integer[] b, int c) {
		super("", EventType.ELEVATOR_BUTTONS);
		buttons = b;
		car = c;
	}
	
	public String toString() {
		return String.format("%s %s %s", super.toString(), car, Arrays.toString(buttons));
	}

	public Integer[] getButtons() {
		return buttons;
	}

	public int getCar() {
		return car;
	}

}
