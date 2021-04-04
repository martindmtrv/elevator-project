package scheduler.GUI;

import event.NotificationEvent;

/**
 * Interface to receive notification updates.
 * 
 * @author: Alex Cameron
 */
public interface NotificationViewListener {
    void handleNotificationUpdate(NotificationEvent e);
}
