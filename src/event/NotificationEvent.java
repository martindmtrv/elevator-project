package event;

import scheduler.GUI.Notification;

public class NotificationEvent extends Event{

    private Notification notification;
    private NotificationEventType notificationEventType;

    public NotificationEvent(Notification notification,NotificationEventType notificationEventType){
        super("",EventType.NOTIFICATION);
        this.notification = notification;
        this.notificationEventType = notificationEventType;
    }

    public Notification getNotification() {
        return notification;
    }

    public NotificationEventType getNotificationEventType() {
        return notificationEventType;
    }
}
