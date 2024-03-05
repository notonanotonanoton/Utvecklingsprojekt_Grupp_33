package boundary;

import control.Client;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientMainView extends Thread {
    private Client client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientMainView(Client client, ObjectOutputStream oos, ObjectInputStream ois) {
        this.client = client;
        this.oos = oos;
        this.ois = ois;
        start();
    }

    @Override
    public void run() {
        System.out.println("Client view active");
        while (!Thread.interrupted()) {
            try {
                System.out.println("Client View: Trying to Read Message");
                handleMessage((Message) ois.readObject());
                System.out.println("Client View: Read Message");
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
                System.out.println("Client View: Message Type Mismatch");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Client View: Error");
            }
        }
    }

    //TODO add full implementation, show message in Boundary/Boundaries
    public void readMessageFromServer(Message message) {
        if (message.getMessageText() != null) {
            String userList = "";
            for(User user : message.getReceivers()) {
                userList += user + ", ";
            }
            System.out.println("Read message text: '" + message.getMessageText() + "' from " + message.getSender() +
                    " to " + userList);
        }
    }

    public void sendMessageToServer(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client View: Message Error: " + ioe.getMessage());
        }
    }

    public void handleMessage(Message message) {
        client.handleMessage(message);
    }
}
