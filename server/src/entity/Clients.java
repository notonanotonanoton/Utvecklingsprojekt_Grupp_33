package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Clients {
    private HashMap<User, Client> clients = new HashMap<>();
    
    public synchronized void put(User user, Client client) {
        clients.put(user, client);
    }

    public synchronized Client get(User user) {
        return clients.get(user);
    }

    public synchronized void removeClient(User user) {
        if (!(clients.containsKey(user))) {
            return;
        }
        clients.remove(user);
    }

    public synchronized ArrayList<User> getAllUsers() {
        return new ArrayList<User>(clients.keySet());
    }
}
