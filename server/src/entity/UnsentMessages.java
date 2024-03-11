package entity;

import shared_entity.message.Message;
import shared_entity.user.User;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The UnsentMessages class manages unsent messages for users who are currently offline.
 * It stores messages for users until they come online to receive them.
 */
public class UnsentMessages {
    private static UnsentMessages INSTANCE;

    private HashMap<User, LinkedList<Message>> unsentMessages = new HashMap<>();

    // Private constructor to prevent external instantiation
    private UnsentMessages() {

    }

    /**
     * Method to get the singleton instance of UnsentMessages.
     * @return The singleton instance of UnsentMessages.
     */
    public static synchronized UnsentMessages getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnsentMessages();
        }
        return INSTANCE;
    }

    /**
     * Method to put a message for a specific user in the unsent messages storage.
     * @param user The user to whom the message is addressed.
     * @param message The message to be stored.
     */
    public synchronized void put(User user, Message message) {
        LinkedList<Message> userMessages = unsentMessages.get(user);
        if (userMessages == null) {
            userMessages = new LinkedList<>();
            unsentMessages.put(user, userMessages);
        }
        userMessages.add(message);
    }

    /**
     * Method to remove all unsent messages for a specific user.
     * @param user The user whose unsent messages are to be removed.
     */
    public synchronized void remove(User user) {
        unsentMessages.remove(user);
    }

    /**
     * Method to get all unsent messages for a specific user.
     * @param user The user whose unsent messages are to be retrieved.
     * @return A LinkedList containing all unsent messages for the specified user.
     */
    public synchronized LinkedList<Message> get(User user) {
        return unsentMessages.get(user);
    }
}

