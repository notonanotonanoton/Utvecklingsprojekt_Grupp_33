package entity;

import shared_entity.message.Message;
import shared_entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> unsentMessages = new HashMap<>();

    public synchronized void put(User user,Message message) {
        ArrayList<Message> userMessages = unsentMessages.get(user);
        if (userMessages == null) {
            userMessages = new ArrayList<>();
            unsentMessages.put(user, userMessages);
        }
        userMessages.add(message);
    }

    public synchronized ArrayList<Message> get(User user) {
        return unsentMessages.get(user); // kan returnerna null
    }
}

