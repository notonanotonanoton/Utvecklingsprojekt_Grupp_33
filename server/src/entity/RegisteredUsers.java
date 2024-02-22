package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUsers {
    private List<User> userList;

    public RegisteredUsers() {
        userList = new ArrayList<>();
    }

    public User findUser(String username) {

        for(User user : userList) {
            if(user.equals(username));
            return user;
        }
        return null;
    }

    public void addUser(User user) {
        userList.addLast(user);
    }
}
