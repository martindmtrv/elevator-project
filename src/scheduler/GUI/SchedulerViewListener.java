package scheduler.GUI;

import event.ElevatorButtonPressEvent;
import event.ElevatorTripUpdateEvent;
import event.FloorButtonPressEvent;
import scheduler.*;

public interface SchedulerViewListener {
    void handleElevatorStatusUpdate(ElevatorTripUpdateEvent e);
    void handleFloorButtonPressUpdate(FloorButtonPressEvent e);
    void handleElevatorButtonPressUpdate(ElevatorButtonPressEvent e);
    void handleElevatorStateUpdate(ElevatorStatus elevatorStatus);
}
