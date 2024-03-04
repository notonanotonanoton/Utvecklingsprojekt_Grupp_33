package boundary;

import control.Client;
import shared_entity.message.Message;

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
            System.out.println("Read message text: '" + message.getMessageText() + "' from " + message.getSender());
        }
    }

    public void sendMessageToServer(Message message) {
        System.out.println("Message Text: " + message.getMessageText());
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client View: Message Error: " + ioe.getMessage());
        }
    }

    public void handleMessage(Message message) {
        System.out.println("message handled");
        client.handleMessage(message);
    }
}
