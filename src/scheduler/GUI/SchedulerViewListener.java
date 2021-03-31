package scheduler.GUI;

import event.ElevatorTripUpdateEvent;

public interface SchedulerViewListener {
    void handleElevatorStatusUpdate(ElevatorTripUpdateEvent e);
}
