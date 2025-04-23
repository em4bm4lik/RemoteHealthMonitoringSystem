package chatandvideoconsultation;

import usermanagement.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatClient {
    private static Scanner scanner = new Scanner(System.in);

    public static void showChat(User sender, User receiver) {
        Chat chat = ChatServer.getChat(sender, receiver);
        for (ChatMessage message: chat.getMessages())
            System.out.println(message.getSender() == sender ? "You: " + message.getContent() + "[ " + message.getTimestamp() + " ]"
                    : receiver.getName() + ": " + message.getContent() + "[ " + message.getTimestamp() + " ]");

        scanner.nextLine(); // Clearing Buffer
        String messageContent;
        while (true) {
            System.out.println("Enter message (or /exit to go back): ");
            messageContent = scanner.nextLine();
            if (messageContent.equals("/exit"))
                return;
            ChatMessage newMessage = new ChatMessage(sender, receiver, messageContent, LocalDateTime.now());
            ChatServer.addMessage(newMessage);
            System.out.println("Message sent successfully!");
        }
    }

    public static void showChatList(User user) {
        ArrayList<Chat> userChats = ChatServer.getUserChats(user);
        if (userChats.isEmpty())
            System.out.println("No chats initiated yet!");
        System.out.println("Your Chat List:\n");
        for (Chat chat: userChats) {
            User other = user.equals(chat.getUser2()) ? chat.getUser1() : chat.getUser2();
            System.out.println(other.getClass().getSimpleName() + " | ID: " + other.getId() + " | Name: " + other.getName());
        }
    }
}
