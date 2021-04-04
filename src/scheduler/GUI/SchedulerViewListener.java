package scheduler.GUI;

import event.ElevatorButtonPressEvent;
import event.ElevatorTripUpdateEvent;
import event.FloorButtonPressEvent;
import scheduler.*;
/**
 * The event listener to deliver update events to the SchedulerView from the SchedulerModel
 * @author: Alex Cameron
 *
 */
public interface SchedulerViewListener {
    void handleElevatorStatusUpdate(ElevatorTripUpdateEvent e);
    void handleFloorButtonPressUpdate(FloorButtonPressEvent e);
    void handleElevatorButtonPressUpdate(ElevatorButtonPressEvent e);
    void handleElevatorStateUpdate(ElevatorStatus elevatorStatus);
}
