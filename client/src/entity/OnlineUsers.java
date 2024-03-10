package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

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
