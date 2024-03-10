package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

public class Receivers {
    private List<User> receiverList;

    public Receivers(User user) {
        receiverList = new ArrayList<>();
        receiverList.addLast(user);
    }

    public void toggleReceiver(User receiver) {
        for(User user : receiverList) {
            if(user.equals(receiver)) {
                receiverList.remove(user);
                System.out.println("Removed " + user + " from Receivers");
                return;
            }
        }
        System.out.println("Added " + receiver + " to Receivers");
        receiverList.addLast(receiver);
    }

    public List<User> getReceiverList() {
        return receiverList;
    }
}
