package chatandvideoconsultation;

import javafx.scene.control.TextInputDialog;
import usermanagement.User;
import utilities.GUIHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class ChatClient {

    public static void showChat(User sender, User receiver) {
        Chat chat = ChatServer.getChat(sender, receiver);

        StringBuilder builder = new StringBuilder();
        for (ChatMessage message: chat.getMessages())
           builder.append(message.getSender() == sender ? "You: " + message.getContent() + "[ " + message.getTimestamp() + " ]" + "\n"
                    : receiver.getName() + ": " + message.getContent() + "[ " + message.getTimestamp() + " ]" + "\n");
        GUIHandler.show(builder.toString());

        String messageContent;
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter message (or /exit to go back): ");
            Optional<String> result = dialog.showAndWait();
            messageContent = result.orElse("/exit");
            if (messageContent.equals("/exit"))
                return;
            ChatMessage newMessage = new ChatMessage(sender, receiver, messageContent, LocalDateTime.now());
            ChatServer.addMessage(newMessage);
            GUIHandler.show("Message sent successfully!");
        }
    }

    public static void showChatList(User user) {
        ArrayList<Chat> userChats = ChatServer.getUserChats(user);
        StringBuilder builder = new StringBuilder("Your Chat List:\n\n");
        if (userChats.isEmpty())
            builder.append("{No chats initiated yet!}");

        for (Chat chat : userChats) {
            User other = user.equals(chat.getUser2())
                    ? chat.getUser1()
                    : chat.getUser2();
            builder.append(other.getClass().getSimpleName())
                    .append(" | ID: ")
                    .append(other.getId())
                    .append(" | Name: ")
                    .append(other.getName())
                    .append("\n");
        }
        GUIHandler.show(builder.toString());

    }
}
