package scheduler.GUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * The class which creates each individual notification panel view that is added to the list of notifications.
 * @author: Alex Cameron
 *
 */
public class NotificationPanel extends JPanel {

    private NotificationView notificationView;
    private Notification notification;
    public final String CLOSE_NOTIFICATION_COMMAND = "close";
    /**
     * Constructor which creates the notification panel view
     * @param notification
     * @param notificationView
     */
    public NotificationPanel(Notification notification, NotificationView notificationView){
        this.notificationView = notificationView; //the notification view
        this.notification = notification; //notification data being created into a view
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //Specify the type of notification 
        JLabel systemTypeLabel;
        if(notification.getNotificationType() == NotificationType.SCHEDULER){
            systemTypeLabel = new JLabel(notification.getNotificationTime()+ " ");
        }else if(notification.getNotificationType() == NotificationType.ELEVATORSUBSYSTEM){
            systemTypeLabel = new JLabel("<html>ELEVATOR<br/>" + notification.getNotificationTime() +"  </html>"); //Omitted 
        }else{
            systemTypeLabel = new JLabel("<html>FLOOR<br/>" + notification.getNotificationTime() +"  </html>"); //Omitted 
        }
        systemTypeLabel.setFont(new Font("Serif", Font.BOLD, 14));
        this.add(systemTypeLabel, BorderLayout.LINE_START);

        //Visual details for notification message displayed in panel
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
    }	
    /**
     * Gets the notification
     * @return The notifcation
     */
    public Notification getNotification(){
        return notification;
    }
}
