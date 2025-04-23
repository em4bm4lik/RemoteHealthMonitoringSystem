package notificationsreminders;

import usermanagement.Patient;
import java.util.ArrayList;

public class ReminderService {
    private  ArrayList<Notifiable> channels = new ArrayList<>();

    public ReminderService() {
        channels.add(new EmailNotification());
        channels.add(new SMSNotification());
    }

    public void sendReminder (Patient receiver, String message) {
        for (Notifiable channel : channels)
            channel.send(receiver, message);
    }
}
