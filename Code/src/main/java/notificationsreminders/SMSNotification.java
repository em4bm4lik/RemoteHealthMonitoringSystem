package notificationsreminders;

import usermanagement.Doctor;
import usermanagement.Patient;
import usermanagement.User;

public class SMSNotification implements Notifiable {


    @Override
    public void send(User receiver, String message) {
        if (receiver instanceof Patient)
            ((Patient) receiver).addNewSMSNotification(message);
        if (receiver instanceof Doctor)
            ((Doctor) receiver).addNewSMSNotification(message);
    }
}
