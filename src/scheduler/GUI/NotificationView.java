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

public class NotificationView extends JPanel implements NotificationViewListener {
    private NotificationModel notificationModel;
    private NotificationController nc;
    private JPanel schedulerListPanel;
    public static final String CLOSE_ALL_NOTIFICATION_SCHEDULER = "closeAllScheduler";

    //Note: Look into JSplitPane
    public NotificationView(){
        super();
        notificationModel = new NotificationModel(); //New Notification model
        notificationModel.addNotificationListener(this); //Add notification view as the listener
        nc = new NotificationController(notificationModel);

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

    public void notifyView(String msg, Date time,NotificationType notificationType){
        this.notificationModel.addNotification(msg,time, notificationType);
    }

    public NotificationController getNotificationController(){
        return nc;
    }

    @Override
    public void handleNotificationUpdate(NotificationEvent e) {
        NotificationEventType notificationEventType = e.getNotificationEventType();
        if(notificationEventType == NotificationEventType.REMOVE_ALL) {
            schedulerListPanel.removeAll();
            this.revalidate();
            this.repaint();
            return;
        }

        Notification notification = e.getNotification();
        if(notification.getNotificationType() == NotificationType.SCHEDULER){ //Elevator
            if(notificationEventType == NotificationEventType.ADD) { //adding notification
                NotificationPanel notificationPanel = new NotificationPanel(notification, this); //create new JPanel being added to list of notifications
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                schedulerListPanel.add(notificationPanel, gbc, 0);
            }else{ //removing notification (NotificationEventType.REMOVE)
                Component removeComponent = null;
                for(Component component: schedulerListPanel.getComponents()){
                    if(component instanceof NotificationPanel){
                        NotificationPanel notificationPanel = (NotificationPanel) component;
                        if(notification == notificationPanel.getNotification()){
                            removeComponent = notificationPanel;
                            break;
                        }
                    }
                }
                if(Objects.nonNull(removeComponent)){
                    schedulerListPanel.remove(removeComponent);
                }
            }
            this.revalidate();
            this.repaint();
        }else if(notification.getNotificationType() == NotificationType.ELEVATORSUBSYSTEM){ //Elevator subsystem
           //TO-DO: Show elevator messages from ElevatorStatus info (Where has yet to be determined)
        }else{ //Floor subsystem
            //TO-DO: Show Button presses notifications (Where has yet to be determined)
        }


    }
}
