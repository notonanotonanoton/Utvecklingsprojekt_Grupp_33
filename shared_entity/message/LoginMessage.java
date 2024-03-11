package shared_entity.message;

import shared_entity.user.User;

/**
 * The LoginMessage class represents a message used for user login in the chat system.
 * It is a subclass of the Message class and is used to indicate a user's login action.
 */
public class LoginMessage extends Message {

    public LoginMessage(User user) {
        setSender(user);
    }
}
