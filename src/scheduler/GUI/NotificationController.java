package scheduler.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationController implements ActionListener {
    private NotificationModel notificationModel;
    public NotificationController(NotificationModel notificationModel){
        this.notificationModel = notificationModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals(NotificationView.CLOSE_ALL_NOTIFICATION_SCHEDULER)) {
                notificationModel.removeAllNotifications();
            }
        } else if(e.getSource() instanceof NotificationPanel){
            System.out.println("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest");
            Notification notification = ((NotificationPanel) e.getSource()).getNotification();
            notificationModel.removeNotification(notification);
        }
    }
}
