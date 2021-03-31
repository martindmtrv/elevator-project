package scheduler.GUI;

import event.NotificationEvent;

/**
 * Interface to receive notification updates.
 */
public interface NotificationViewListener {
    void handleNotificationUpdate(NotificationEvent e);
}
