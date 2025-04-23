package notificationsreminders;

import usermanagement.User;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;


public class EmailNotification implements Notifiable {

    @Override
    public void send(User receiver, String message) {
        String recipientEmail = receiver.getEmail();
        String senderEmail = "abdullahbinsaffi@gmail.com";
        String senderPassword = "msejueuyyurordxi";
        // Setting properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); // Doing this to enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Doing this to enable STARTTLS
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Using Gmail's SMTP server
        properties.put("mail.smtp.port", "587"); // Port for TLS

        // Authenticating sender
        Session session = Session.getInstance(properties, new Authenticator() { // Creating anonymous subclass can be EmailAuthenticator
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Composing the email
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail)); // From
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail)); // To
            mimeMessage.setSubject("Hospital Notification"); // Subject
            mimeMessage.setText(message); // Body

            // Sending the email
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}
