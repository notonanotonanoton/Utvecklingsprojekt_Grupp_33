package shared_entity.message;

import shared_entity.user.User;

import java.io.Serializable;

public class LoginMessage extends Message implements Serializable {

    private User user;
    public LoginMessage(User user) {
        setSender(user);
    }

    public void setSender(User user) {
        this.user = user;
    }

    public User getSender() {
        return this.user;
    }
}
