package chatandvideoconsultation;

import usermanagement.User;

public class VideoCall {

    public static String generateLink(User caller, User called) {
        return "www.meet.google.com/" + caller.getId() + caller.getName() + "Calls" +
                called.getId() + called.getName();
    }
}
