package scheduler.GUI;

import event.NotificationEvent;
import event.NotificationEventType;
import main.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;
/**
 * The view for the notification displayed for the scheduler
 * @author Alex Cameron
 *
 */
public class NotificationView extends JPanel implements NotificationViewListener {
    private NotificationModel notificationModel;
    private NotificationController nc;
    private JPanel schedulerListPanel;
    public static final String CLOSE_ALL_NOTIFICATION_SCHEDULER = "closeAllScheduler";

    /**
     * The NotificationView Constructor
     */
    public NotificationView(){
        super();
        notificationModel = new NotificationModel(); //New Notification model
        notificationModel.addNotificationListener(this); //Add notification view as the listener
        nc = new NotificationController(notificationModel); //create a NotificationController

        //Scheduler Notification Panel
        this.setLayout(new BorderLayout());
        schedulerListPanel = new JPanel();
        schedulerListPanel.setLayout(new GridBagLayout());
        JLabel schedulerLabel = new JLabel("Scheduler Event Log");
        schedulerLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JButton closeAllScheduler = new JButton("Close All");
        closeAllScheduler.setFocusPainted(false);
        closeAllScheduler.setActionCommand(CLOSE_ALL_NOTIFICATION_SCHEDULER);
        closeAllScheduler.addActionListener(nc);
        this.add(schedulerLabel,BorderLayout.PAGE_START);
        this.add(closeAllScheduler, BorderLayout.PAGE_END);
        this.add(new JScrollPane(schedulerListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    }
    /**
     * Adds a new notification to the NotificationModel.
     * @param msg - The message of notification
     * @param time - The time of the notification
     * @param notificationType - The notification type
     */
    public void notifyView(String msg, Date time,NotificationType notificationType){
        this.notificationModel.addNotification(msg,time, notificationType);
    }
    /**
     * Getter method for the NotificationController
     * @return The NotificationController
     */
    public NotificationController getNotificationController(){
        return nc;
    }

    @Override
    public void handleNotificationUpdate(NotificationEvent e) {
        NotificationEventType notificationEventType = e.getNotificationEventType();
        //If notification event is remove all then all notifications in the list will be removed from the view
        if(notificationEventType == NotificationEventType.REMOVE_ALL) {
            schedulerListPanel.removeAll(); //remove all notifications
            this.revalidate();
            this.repaint(); //revalidate frame
            return;
        }

        Notification notification = e.getNotification();
        if(notification.getNotificationType() == NotificationType.SCHEDULER){ //Notification type = Scheduler
            if(notificationEventType == NotificationEventType.ADD) { //adding notification
                NotificationPanel notificationPanel = new NotificationPanel(notification, this); //create new JPanel being added to list of notifications
                GridBagConstraints gbc = new GridBagConstraints(); //create layout
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                schedulerListPanel.add(notificationPanel, gbc, 0);
            }
            this.revalidate();
            this.repaint();
        }else if(notification.getNotificationType() == NotificationType.ELEVATORSUBSYSTEM){ //Elevator subsystem
           //No notifications for Elevator Subsystem
        }else{ //Floor subsystem
            //No notifications for Floor Subsystem
        }
    }
}
