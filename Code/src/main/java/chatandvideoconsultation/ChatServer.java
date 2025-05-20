package chatandvideoconsultation;

import usermanagement.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer {
    private static HashMap<String, Chat> chats = new HashMap<>();

    public static Chat getChat(User sender, User receiver) {
        String chatId = generateUniqueChatId(sender, receiver);
        if (!chats.containsKey(chatId))
            createNewChat(chatId, sender, receiver);
        return chats.get(chatId);
    }

    public static ArrayList<Chat> getUserChats(User user) {
        ArrayList<Chat> usersChats = new ArrayList<>();
        for (Chat chat: chats.values())
            if (chat.getUser1().equals(user) || chat.getUser2().equals(user))
                usersChats.add(chat);
        return usersChats;
    }

    public static void addMessage(ChatMessage newMessage) {
        String chatId = generateUniqueChatId(newMessage.getSender(), newMessage.getReceiver());
        chats.get(chatId).addMessage(newMessage);
    }

    private static void createNewChat(String chatId, User sender, User receiver) {
        Chat chat = new Chat(chatId, sender, receiver);
        chats.put(chat.getChatId(), chat);
    }

    public static String generateUniqueChatId (User sender, User receiver) {
        return Math.min(sender.getId(), receiver.getId()) + "-" + Math.max(sender.getId(), receiver.getId());
    }

    public static void setChats(HashMap<String, Chat> chats) {
        ChatServer.chats = chats;
    }

    public static HashMap<String, Chat> getChats() {
        return chats;
    }
}
