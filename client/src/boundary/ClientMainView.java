package boundary;

import control.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientMainView extends Thread {
    private Client client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ClientMainFrame mainFrame;

    public ClientMainView(Client client, ObjectOutputStream oos, ObjectInputStream ois) {
        this.client = client;
        this.oos = oos;
        this.ois = ois;
        mainFrame = new ClientMainFrame(this);
        start();
    }

    @Override
    public void run() {
        System.out.println("Client view active");
        while (!Thread.interrupted()) {
            try {
                System.out.println("Client View: Trying to Read Message");
                handleMessage(ois.readObject());
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


    public void sendMessageToServer(Object messageObject) {
        try {
            oos.writeObject(messageObject);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client View: Message Error: " + ioe.getMessage());
        }
    }

    public void handleMessage(Object messageObject) {
        client.handleMessage(messageObject);
    }

    public void addMessageRow(Object[] messageInfo) {
        mainFrame.addMessageRow(messageInfo);
    }

    public void createMessage(String messageText, ImageIcon messageImage) {
        client.assembleUserToUserMessage(messageText, messageImage);
    }
}
