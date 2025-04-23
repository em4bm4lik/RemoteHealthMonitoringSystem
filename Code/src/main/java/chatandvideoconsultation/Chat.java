package chatandvideoconsultation;

import usermanagement.User;

import java.util.ArrayList;

public class Chat {
    private String chatId;
    private User user1;
    private User user2;
    private ArrayList<ChatMessage> messages = new ArrayList<>();

    public Chat(String chatId, User user1, User user2) {
        this.chatId = chatId;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getChatId() {
        return chatId;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage newMessage) {
        messages.add(newMessage);
    }
}
