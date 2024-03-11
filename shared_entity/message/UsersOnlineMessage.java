package shared_entity.message;

import shared_entity.user.User;

import java.util.List;

/**
 * The UsersOnlineMessage class represents a message sent to inform clients about users who are currently online.
 * It is a subclass of the Message class and is used to broadcast the list of online users to clients.
 */
public class UsersOnlineMessage extends Message {

    public UsersOnlineMessage(List<User> userList) {
        setReceivers(userList);
    }
    @Override
    public String toString() {
        return "Sent UsersOnlineMessage: " + super.getReceivers() + " to clients with username: " + super.getReceivers();
    }
}
