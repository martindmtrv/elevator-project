package scheduler.GUI;

import event.*;

import java.util.ArrayList;
import java.util.Date;

public class NotificationModel {
    ArrayList<Notification> notificationArrayList;
    ArrayList<NotificationViewListener> notificationViewListeners;

    /**
     * New notification model to handle all notifications / event updates from the subsystems
     */
    public NotificationModel(){
        this.notificationArrayList = new ArrayList<>();
        this.notificationViewListeners = new ArrayList<>();
    }
    /**
     * Add a new notification to the list of current notifications.
     * @param message Message to display in the notification.
     * @param notificationType Notification type.
     */
    public void addNotification(String message,Date time, NotificationType notificationType){
        Notification notification = new Notification(message,time,notificationType);
        this.notificationArrayList.add(notification);
        //update the listeners
        updateNotificationListeners(new NotificationEvent(notification,NotificationEventType.ADD));
    }

    /**
     * Remove a notification from thhe list of current notifications.
     * @param notification Notification to remove.
     */
    public void removeNotification(Notification notification){
        this.notificationArrayList.remove(notification);
        updateNotificationListeners(new NotificationEvent(notification, NotificationEventType.REMOVE));
    }

    /**
     * Remove all current notifications.
     */
    public void removeAllNotifications(){
        this.notificationArrayList.clear();
        updateNotificationListeners(new NotificationEvent(null, NotificationEventType.REMOVE_ALL));
    }

    /**
     * Add a new notification listener.
     * @param nvl Notification listener.
     */
    public void addNotificationListener(NotificationViewListener nvl){
        this.notificationViewListeners.add(nvl);
    }

    /**
     * Update listeners.
     * @param notificationEvent Event to pass in update.
     */
    private void updateNotificationListeners(NotificationEvent notificationEvent){
        for(NotificationViewListener nvl: notificationViewListeners){
            nvl.handleNotificationUpdate(notificationEvent);
        }
    }
}
