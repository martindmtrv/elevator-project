package scheduler.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Controller for Notifications
 * @author: Alex Cameron
 *
 */
public class NotificationController implements ActionListener {
    private NotificationModel notificationModel;
    /**
     *  NotificationController constructor
     * @param notificationModel - The Notification model
     */
    public NotificationController(NotificationModel notificationModel){
        this.notificationModel = notificationModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) { //if a JButton is clicked then close all notifications on scheduler
            if (e.getActionCommand().equals(NotificationView.CLOSE_ALL_NOTIFICATION_SCHEDULER)) {
                notificationModel.removeAllNotifications();
            }
        } else if(e.getSource() instanceof NotificationPanel){ //removing an instance of a notification (NOT IMPLEMENTED/ NOT USEFUl)
            Notification notification = ((NotificationPanel) e.getSource()).getNotification();
            notificationModel.removeNotification(notification);
        }
    }
}
