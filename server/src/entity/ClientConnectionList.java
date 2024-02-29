package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public final class ClientConnectionList {
    private static ClientConnectionList INSTANCE;
    private HashMap<User, ClientConnection> clients = new HashMap<>();

    private ClientConnectionList() {

    }

    public synchronized static ClientConnectionList getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ClientConnectionList();
        }
        return INSTANCE;
    }
    
    public synchronized void put(User user, ClientConnection clientConnection) {
        System.out.println("Putting " + user.getUserName() + " and " + clientConnection.toString() + " in Clients");
        clients.put(user, clientConnection);
    }

    public synchronized ClientConnection get(User user) {
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
