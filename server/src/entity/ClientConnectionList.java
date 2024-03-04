package entity;

import shared_entity.user.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

public final class ClientConnectionList {
    private static ClientConnectionList INSTANCE;
    private HashMap<User, ClientConnection> clients = new HashMap<>();

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private ClientConnectionList() {

    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
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
        pcs.firePropertyChange("clients", null, null);
    }

    public synchronized ClientConnection get(User user) {
        return clients.get(user);
    }

    public synchronized void removeClient(User user) {
        if (!(clients.containsKey(user))) {
            return;
        }
        clients.remove(user);
        pcs.firePropertyChange("clients", null, null);
    }

    public synchronized ArrayList<User> getAllUsers() {
        return new ArrayList<>(clients.keySet());
    }
}
