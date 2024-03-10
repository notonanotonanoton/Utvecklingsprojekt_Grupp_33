package boundary;

import control.Client;

import javax.swing.*;
import java.awt.*;
import java.net.SocketException;
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
            } catch (SocketException se) {
                System.out.println("Socket closed. Exiting client view thread.");
                break;
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
            oos.reset();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client View: Message Error: " + ioe.getMessage());
        }
    }

    public void notifyServerOnExit() {
        client.assembleExitMessage();
    }
    public void saveContactsToFile() {
        client.saveContactsToFile();
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

    public void updateOnlineUsersGUI(Object[][] usersInfo) {
        mainFrame.addUserModel(usersInfo);
    }

    public void updateContactsGUI(Object[][] contactInfo) {
        mainFrame.addContactsModel(contactInfo);
    }

    public void updateReceiversGUI(Object[][] receiversInfo) {
        mainFrame.addReceiverModel(receiversInfo);
    }

    public void addContact(String username) {
        client.addContact(username);
    }

    public void removeContact(String username) {
        client.removeContact(username);
    }

    public void toggleReceiver(String username) {
        client.toggleReceiver(username);
    }
}
