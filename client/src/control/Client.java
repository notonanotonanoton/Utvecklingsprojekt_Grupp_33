package control;

import boundary.ClientMainView;
import entity.ClientContacts;
import entity.OnlineUsers;
import entity.Receivers;
import shared_entity.message.Message;
import shared_entity.message.UsersOnlineMessage;
import shared_entity.user.User;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

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
        //TODO load ClientContacts from file here or inside ClientContacts class?
        clientContacts = new ClientContacts();
        receivers = new Receivers(user);
        mainView = new ClientMainView(this, oos, ois);
        updateReceiversGUI();
    }

    public void handleMessage(Object messageObject) {
        Message message = (Message) messageObject;
        message.setReceivedByUser(); // correct?
        if (message instanceof UsersOnlineMessage) {
            onlineUsers.setUserList(message.getReceivers());
            updateOnlineUsersGUI();
        } else {
            displayMessageFromServer(message);
        }
    }

    public void assembleUserToUserMessage(String messageText, ImageIcon messageImage) {
        Message message = new Message();
        if (messageText != null) {
            message.setMessageText(messageText);
        }
        if (messageImage != null) {
            message.setMessageImage(messageImage);
        }
        message.setSender(user);
        //TODO get receivers from GUI
        message.setReceivers(receivers.getReceiverList());
        mainView.sendMessageToServer(message);
        System.out.println("Message '" + message.getMessageText() + "' sent to server");
    }

    public void displayMessageFromServer(Message message) {
        String messageText = message.getMessageText();
        ImageIcon messageIcon = message.getMessageImage();
        String username = message.getSender().getUserName();
        String formattedUsername = "From: " + username;
        ImageIcon userIcon = message.getSender().getUserIcon();

        //TODO fix better null handling
        if (messageText == null) {
            messageText = "";
        }
        if (messageIcon == null) {
            messageIcon = new ImageIcon();
        }
        if (userIcon == null) {
            userIcon = new ImageIcon();
        }

        Object[] messageInfo = new Object[]{messageText, messageIcon, formattedUsername, userIcon};

        mainView.addMessageRow(messageInfo);
    }

    public void addContact(String username) {
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

    public void removeContact(String username) {
        if(username.equals(this.user.getUserName())) {
            return;
        }
        clientContacts.removeContact(username);
        updateContactsGUI();
    }

    public void toggleReceiver(String username) {
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

    public void updateReceiversGUI() {
        List<User> receiverList = receivers.getReceiverList();
        Object[][] receiverInfo = new Object[receiverList.size()][1];
        for (int i = 0; i < receiverInfo.length; i++) {
                receiverInfo[i][0] = receiverList.get(i).getUserName();
        }
        mainView.updateReceiversGUI(receiverInfo);
    }
}
