package entity;

import shared_entity.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receivers {
    private ArrayList<User> receiverList;

    public Receivers(User user) {
        receiverList = new ArrayList<>();
        receiverList.addLast(user);
    }

    public void toggleReceiver(User receiver) {
        for(User names : receiverList) {
            System.out.println("receivers: "+names.getUserName());
        }
        for(User user : receiverList) {
            if(user.getUserName().equals(receiver.getUserName())) {
                receiverList.remove(user);
                System.out.println("Removed " + user + " from Receivers");
                for(User names : receiverList) {
                    System.out.println("receivers: "+names.getUserName());
                }
                return;
            }
        }
        System.out.println("Added " + receiver + " to Receivers");
        receiverList.addLast(receiver);
        for(User names : receiverList) {
            System.out.println("receivers: "+names.getUserName());
        }
    }

    public List<User> getReceiverList() {
        return receiverList;
    }
}
