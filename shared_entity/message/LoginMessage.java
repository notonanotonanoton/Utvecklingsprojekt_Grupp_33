package shared_entity.message;

import shared_entity.user.User;

public class LoginMessage extends Message {

    public LoginMessage(User user) {
        setSender(user);
    }

}
