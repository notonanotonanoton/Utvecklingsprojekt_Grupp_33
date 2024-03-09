package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

public class Receivers {
    private List<User> receiverList;

    public Receivers() {
        receiverList = new ArrayList<>();
    }

    public void addReceiver(User receiver) {
        receiverList.addLast(receiver);
    }

    public void removeReceiver(String username) {
        for (User user : receiverList) {
            if (user.getUserName().equals(username)) {
                receiverList.remove(user);
                return;
            }
        }
    }

    public List<User> getReceiverList() {
        return receiverList;
    }
}
