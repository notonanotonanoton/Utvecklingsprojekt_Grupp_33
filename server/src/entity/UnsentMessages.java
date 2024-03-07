package entity;

import shared_entity.message.Message;
import shared_entity.user.User;

import java.util.HashMap;
import java.util.LinkedList;

public class UnsentMessages {
    private static UnsentMessages INSTANCE;

    private HashMap<User, LinkedList<Message>> unsentMessages = new HashMap<>();

    private UnsentMessages() {

    }

    public static synchronized UnsentMessages getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnsentMessages();
        }
        return INSTANCE;
    }

    public synchronized void put(User user, Message message) {
        LinkedList<Message> userMessages = unsentMessages.get(user);
        if (userMessages == null) {
            userMessages = new LinkedList<>();
            unsentMessages.put(user, userMessages);
        }
        userMessages.add(message);
    }

    public synchronized LinkedList<Message> get(User user) {
        return unsentMessages.get(user); // kan returnerna null
    }
}

