package scheduler.GUI;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/**
 * Notification class that contains a message, time and notification type. (NotificationType ENUM)
 * @author: Alex Cameron
 *
 */
public class Notification {

    private String message;
    private Date time;
    private NotificationType notificationType;
    /**
     * Notification constructor grouping all data together
     * @param msg - Notification Message
     * @param time - Time the event occurs
     * @param type - Notification type
     */
    public Notification(String msg,Date time, NotificationType type){
        this.message = msg;
        this.time = time;
        this.notificationType = type;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getNotificationTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(time);
    }

}
