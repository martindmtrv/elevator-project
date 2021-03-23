package event;

/**
 * Fault event to generalize all elevator faults
 * @author Martin Dimitrov
 *
 */
public class Fault extends Event {
	private static final long serialVersionUID = -3384670291048854764L;
	private FaultType faultType;
	private int car;
	
	/**
	 * Create a new fault supplying the car to place it on and the type of fault
	 * @param eid - which car should this fault affect
	 * @param f - what type of fault is this
	 */
	public Fault(int eid, FaultType f) {
		super("", EventType.FAULT);
		faultType = f;
		car = eid;
	}

	public FaultType getFaultType() {
		return faultType;
	}

	public int getCar() {
		return car;
	}

}
