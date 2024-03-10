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

    //true = add, false = remove
    public boolean toggleReceiver(User receiver) {
        for(User user : receiverList) {
            if(user.equals(receiver)) {
                receiverList.remove(user);
                return false;
            }
        }
        receiverList.addLast(receiver);
        return true;
    }

    public List<User> getReceiverList() {
        return receiverList;
    }
}
