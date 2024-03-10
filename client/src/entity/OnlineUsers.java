package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

//updated by Server sending List<User> of all online users and Client creating a new OnlineUsers
public class OnlineUsers {
    List<User> userList;

    public OnlineUsers() {
        userList = new ArrayList<>();
    }

    public OnlineUsers(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }
}
