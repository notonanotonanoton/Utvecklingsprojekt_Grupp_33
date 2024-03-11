package shared_entity.message;


import shared_entity.user.User;

// The ExitMessage class represents a message used to notify about a user's exit from the chat system.
public class ExitMessage extends Message {

    public ExitMessage(User user) {
        super.setSender(user);
    }

    @Override
    public String toString() {
        return "Sent ExitMessage to clients with username: " + super.getReceivers();
    }
}
