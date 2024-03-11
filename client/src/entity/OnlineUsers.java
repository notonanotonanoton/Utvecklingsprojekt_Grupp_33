package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The OnlineUsers class represents a list of users currently online in the chat system.
 * It provides methods to get and set the list of online users.
 */
public class OnlineUsers {
    private List<User> userList;

    public OnlineUsers() {
        userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
