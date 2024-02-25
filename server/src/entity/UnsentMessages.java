package entity;

import shared_entity.message.Message;
import shared_entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UnsentMessages {
    private HashMap<User, LinkedList<Message>> unsentMessages = new HashMap<>();

    public synchronized void put(User user,Message message) {
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

