package shared_entity.message;

import shared_entity.user.User;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.List;

public class UsersOnlineMessage extends Message implements Serializable {

    public UsersOnlineMessage(List<User> userList) {
        setReceivers(userList);
    }
    @Override
    public String toString() {
        return "Sent UsersOnlineMessage: " + super.getReceivers();
    }
}
