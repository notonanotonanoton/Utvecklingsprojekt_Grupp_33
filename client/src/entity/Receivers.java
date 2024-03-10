package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

public class Receivers {
    private List<User> receiverList;

    public Receivers() {
        receiverList = new ArrayList<>();
    }

    //true = add, false = remove
    public boolean addOrRemoveReceiver(User receiver) {
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
