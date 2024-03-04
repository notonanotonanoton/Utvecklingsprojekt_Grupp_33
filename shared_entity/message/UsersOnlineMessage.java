package shared_entity.message;

import shared_entity.user.User;

import java.util.List;

public class UsersOnlineMessage extends Message {
    public UsersOnlineMessage(List<User> userList) {
        setReceivers(userList);
    }
}
