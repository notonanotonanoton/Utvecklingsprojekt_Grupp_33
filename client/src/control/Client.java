package control;

import boundary.ClientMainView;
import entity.ClientContacts;
import entity.OnlineUsers;
import entity.Receivers;
import shared_entity.message.ExitMessage;
import shared_entity.message.Message;
import shared_entity.message.UsersOnlineMessage;
import shared_entity.user.User;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Represents a client in the chat system.
 * Each client is associated with a user, maintains communication with the server,
 * and manages user interactions through the main view.
 */
public class Client {
    private User user;
    private Socket clientSocket;
    private ClientMainView mainView;
    private OnlineUsers onlineUsers;
    private ClientContacts clientContacts;
    private Receivers receivers;

    public Client(User user, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.user = user;
        this.clientSocket = clientSocket;
        onlineUsers = new OnlineUsers();
        clientContacts = new ClientContacts(user.getUserName());
        System.out.println("Loaded clients for user: "  + user.getUserName() + ": " + clientContacts.getContactList());
        receivers = new Receivers(user);
        mainView = new ClientMainView(this, oos, ois);
        updateReceiversGUI();
        updateContactsGUI();
    }

    /**
     * Handles incoming messages from the server.
     * Updates the online users list if the message is a UsersOnlineMessage.
     * Displays the message in the main view if it's not a UsersOnlineMessage.
     * @param messageObject The message received from the server
     */
    public void handleMessage(Object messageObject) {
        Message message = (Message) messageObject;
        message.setReceivedByUser();
        if (message instanceof UsersOnlineMessage) {
            onlineUsers.setUserList(message.getReceivers());
            updateOnlineUsersGUI();
        } else {
            System.out.println("display message called!!: " + user);
            displayMessageFromServer(message);
        }
    }

    /**
     * Assembles and sends a user-to-user message to the server.
     * @param messageText The text content of the message
     * @param messageImage The image attached to the message
     */
    public void assembleUserToUserMessage(String messageText, ImageIcon messageImage) {
        Message message = new Message();
        if (messageText != null) {
            message.setMessageText(messageText);
        }
        if (messageImage != null) {
            message.setMessageImage(messageImage);
        }
        message.setSender(this.user);
        message.setReceivers(receivers.getReceiverList());
        mainView.sendMessageToServer(message);
        System.out.println("Message '" + message.getMessageText() + "' sent to server");
    }

    /**
     * Assembles and sends an exit message to the server.
     * Closes the client after sending the message.
     */
    public void assembleExitMessage() {
        ExitMessage exitMessage = new ExitMessage(user);
        mainView.sendMessageToServer(exitMessage);
        closeClient();
    }

    /**
     * Saves the client's contacts to a file.
     */
    public void saveContactsToFile() {
        clientContacts.saveUsersToFile();
    }

    /**
     * Displays a message received from the server in the main view.
     * @param message The message received from the server
     */
    public void displayMessageFromServer(Message message) {
        String messageText = message.getMessageText();
        ImageIcon messageIcon = message.getMessageImage();
        String username = message.getSender().getUserName();
        String formattedUsername = "From: " + username;
        ImageIcon userIcon = message.getSender().getUserIcon();

        if (messageText == null) {
            messageText = "";
        }
        if (messageIcon == null) {
            messageIcon = new ImageIcon();
        }
        if (username == null) {
            username = "";
        }
        if (userIcon == null) {
            userIcon = new ImageIcon();
        }

        Object[] messageInfo = new Object[]{messageText, messageIcon, formattedUsername, userIcon};

        mainView.addMessageRow(messageInfo);
    }

    /**
     * Adds a contact to the client's contact list.
     * @param username The username of the contact to add
     */
    public void addContact(String username) {
        System.out.println("ADD CONTACT CALLED");
        if(username.equals(this.user.getUserName())) {
            return;
        }
        for (User contactUser : clientContacts.getContactList()) {
            if (contactUser.getUserName().equals(username)) {
                return;
            }
        }
        for (User onlineUser : onlineUsers.getUserList()) {
            if (onlineUser.getUserName().equals(username)) {
                clientContacts.addContact(onlineUser);
                updateContactsGUI();
                return;
            }
        }
    }

    /**
     * Removes a contact from the client's contact list.
     * @param username The username of the contact to remove
     */
    public void removeContact(String username) {
        System.out.println("REMOVE CONTACT CALLED");
        if(username.equals(this.user.getUserName())) {
            return;
        }
        clientContacts.removeContact(username);
        updateContactsGUI();
    }

    /**
     * Toggles a receiver's status in the receiver list.
     * @param username The username of the receiver
     */
    public void toggleReceiver(String username) {
        System.out.println("TOGGLE RECEIVER CALLED");
        if(username.equals(this.user.getUserName())) {
            return;
        }
        for (User userContact : clientContacts.getContactList()) {
            if (userContact.getUserName().equals(username)) {
                receivers.toggleReceiver(userContact);
                updateReceiversGUI();
                return;
            }
        }
        for (User userOnline : onlineUsers.getUserList()) {
            if (userOnline.getUserName().equals(username)) {
                receivers.toggleReceiver(userOnline);
                updateReceiversGUI();
                return;
            }
        }
    }

    /**
     * Updates the GUI with the current list of online users.
     */
    public void updateOnlineUsersGUI() {
        List<User> userList = onlineUsers.getUserList();
        Object[][] userInfo = new Object[userList.size()][3];
        for (int i = 0; i < userInfo.length; i++) {
            for (int j = 0; j < userInfo[0].length; j++) {
                if (j == 0) {
                    userInfo[i][j] = userList.get(i).getUserIcon();
                } else if (j == 1) {
                    userInfo[i][j] = userList.get(i).getUserName();
                } else if (j == 2) {
                    userInfo[i][j] = "+CONTACT";
                }
            }
        }
        mainView.updateOnlineUsersGUI(userInfo);
    }

    /**
     * Updates the GUI with the current list of contacts.
     */
    public void updateContactsGUI() {
        List<User> contactList = clientContacts.getContactList();
        Object[][] contactInfo = new Object[contactList.size()][3];

        for (int i = 0; i < contactInfo.length; i++) {
            for (int j = 0; j < contactInfo[0].length; j++) {
                if (j == 0) {
                    contactInfo[i][j] = contactList.get(i).getUserIcon();
                } else if (j == 1) {
                    contactInfo[i][j] = contactList.get(i).getUserName();
                } else if (j == 2) {
                    contactInfo[i][j] = "-CONTACT";
                }
            }
        }
        mainView.updateContactsGUI(contactInfo);
    }

    /**
     * Updates the GUI with the current list of receivers.
     */
    public void updateReceiversGUI() {
        List<User> receiverList = receivers.getReceiverList();
        Object[][] receiverInfo = new Object[receiverList.size()][1];
        for (int i = 0; i < receiverInfo.length; i++) {
                receiverInfo[i][0] = receiverList.get(i).getUserName();
        }
        mainView.updateReceiversGUI(receiverInfo);
    }

    /**
     * Closes the client by closing the client socket.
     */
    public void closeClient() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
