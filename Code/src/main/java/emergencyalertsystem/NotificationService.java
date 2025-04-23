package emergencyalertsystem;
import notificationsreminders.EmailNotification;
import notificationsreminders.Notifiable;
import notificationsreminders.SMSNotification;
import usermanagement.Doctor;

import java.util.ArrayList;


public class NotificationService {
    private ArrayList<Notifiable> channels = new ArrayList<>();

    public NotificationService() {
        channels.add(new EmailNotification());
        channels.add(new SMSNotification());
    }

    public void sendNotifications(Doctor receiver, String message)  {
        for (Notifiable channel : channels)
            channel.send(receiver, message);
    }
}
