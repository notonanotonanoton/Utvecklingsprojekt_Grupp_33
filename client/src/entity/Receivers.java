package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The Receivers class manages a list of users who are intended recipients of a message.
 * It provides methods to add, remove, and retrieve receivers.
 */
public class Receivers {
    private ArrayList<User> receiverList;

    public Receivers(User user) {
        receiverList = new ArrayList<>();
        receiverList.addLast(user);
    }

    /**
     * Method to toggle the status of a receiver in the receiver list.
     * If the receiver is present in the list, it will be removed; otherwise, it will be added.
     * @param receiver The receiver to be toggled.
     */
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
