package notificationsreminders;

import usermanagement.User;

public interface Notifiable {
    void send(User receiver, String message);
}
