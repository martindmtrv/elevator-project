package scheduler.GUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationPanel extends JPanel {

    private NotificationView notificationView;
    private Notification notification;
    public final String CLOSE_NOTIFICATION_COMMAND = "close";

    public NotificationPanel(Notification notification, NotificationView notificationView){
        this.notificationView = notificationView;
        this.notification = notification;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel systemTypeLabel;
        if(notification.getNotificationType() == NotificationType.SCHEDULER){
            systemTypeLabel = new JLabel(notification.getNotificationTime()+ " ");
        }else if(notification.getNotificationType() == NotificationType.ELEVATORSUBSYSTEM){
            systemTypeLabel = new JLabel("<html>ELEVATOR<br/>" + notification.getNotificationTime() +"  </html>");
        }else{
            systemTypeLabel = new JLabel("<html>FLOOR<br/>" + notification.getNotificationTime() +"  </html>");
        }
        systemTypeLabel.setFont(new Font("Serif", Font.BOLD, 14));
        this.add(systemTypeLabel, BorderLayout.LINE_START);

        //change below
        JTextArea notificationTextArea = new JTextArea(2, 20);
        notificationTextArea.setText(notification.getMessage());
        notificationTextArea.setWrapStyleWord(true);
        notificationTextArea.setLineWrap(true);
        notificationTextArea.setOpaque(false);
        notificationTextArea.setEditable(false);
        notificationTextArea.setFocusable(false);
        notificationTextArea.setFont(new Font("Serif", Font.PLAIN, 14));
        notificationTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(notificationTextArea, BorderLayout.CENTER);
//     (TEST) DOES NOT WORK YET: Cant figure out how to close the notifications individually
//        JButton closeNotificationButton = new JButton("X");
//        closeNotificationButton.setFocusPainted(false);
//        closeNotificationButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                notificationView.getNotificationController().actionPerformed(
//                        new ActionEvent(this, ActionEvent.ACTION_PERFORMED, CLOSE_NOTIFICATION_COMMAND));
//            }
//        });
//        this.add(closeNotificationButton, BorderLayout.LINE_END);
    }

    public Notification getNotification(){
        return notification;
    }
}
