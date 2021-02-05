import java.util.Arrays;

/**
 * @author Martin Dimitrov
 *
 */
public class ElevatorButtonPressEvent extends Event {
	private Integer[] buttons;
	private int car;
	
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
