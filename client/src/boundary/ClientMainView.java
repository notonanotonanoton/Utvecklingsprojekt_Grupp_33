package boundary;

import control.Client;
import shared_entity.message.Message;

import javax.swing.*;
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
        //TODO remove temporary Message send test
        sendMessageToServer("Hej hej!", null);
        while (!Thread.interrupted()) {
            try {
                Message message = ((Message) ois.readObject());
                readMessageFromServer(message);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Client View: Message Type Mismatch");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Client View: Error");
            }
        }
    }

    //TODO add full implementation, show message in Boundary/Boundaries and send Message to Client
    public void readMessageFromServer(Message message) {
        System.out.println("Read message text: " + message.getMessageText());
    }

    public void sendMessageToServer(String messageText, ImageIcon messageImage) {
        System.out.println("Message Text: " + messageText);
        Message message = new Message(messageText);
        try {
            oos.writeObject(message);
            oos.flush();
            System.out.println("Client Boundary: Message Sent: " + message.getMessageText());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client Boundary: Message Error: " + ioe.getMessage());
        }
    }
}
