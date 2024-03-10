package shared_entity.message;


import shared_entity.user.User;

public class ExitMessage extends Message {

    public ExitMessage(User user) {
        super.setSender(user);
    }

    @Override
    public String toString() {
        return "Sent ExitMessage to clients with username: " + super.getReceivers();
    }
}
