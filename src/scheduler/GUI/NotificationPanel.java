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
        JLabel systemTypeLabel;
        if(notification.getNotificationType() == NotificationType.SCHEDULER){
            systemTypeLabel = new JLabel("<html>SCHEDULER<br/>" + notification.getNotificationTime() +"  </html>");
        }else if(notification.getNotificationType() == NotificationType.ELEVATORSUBSYSTEM){
            systemTypeLabel = new JLabel("<html>ELEVATOR<br/>" + notification.getNotificationTime() +"  </html>");
        }else{
            systemTypeLabel = new JLabel("<html>FLOOR<br/>" + notification.getNotificationTime() +"  </html>");
        }
        systemTypeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(systemTypeLabel, BorderLayout.LINE_START);

        //change below
        JTextArea notificationTextArea = new JTextArea(2, 20);
        notificationTextArea.setText(notification.getMessage());
        notificationTextArea.setWrapStyleWord(true);
        notificationTextArea.setLineWrap(true);
        notificationTextArea.setOpaque(false);
        notificationTextArea.setEditable(false);
        notificationTextArea.setFocusable(false);
        notificationTextArea.setBackground(UIManager.getColor("Label.background"));
        notificationTextArea.setFont(UIManager.getFont("Label.font"));
        notificationTextArea.setBorder(UIManager.getBorder("Label.border"));
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
        this.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
    }

    public Notification getNotification(){
        return notification;
    }
}
