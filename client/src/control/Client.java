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
        clientContacts = new ClientContacts();
        receivers = new Receivers();
        mainView = new ClientMainView(this, oos, ois);
    }

    //TODO add full implementation when possible
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
        System.out.println("Username: " + username);
        ImageIcon userIcon = message.getSender().getUserIcon();

        System.out.println("User Icon: " + userIcon);

        //TODO fix better null handling
        if (messageText == null) {
            messageText = " ";
        }
        if (messageIcon == null) {
            messageIcon = new ImageIcon();
        }
        if (username == null) {
            username = " ";
        }
        if (userIcon == null) {
            userIcon = new ImageIcon();
        }

        Object[] messageInfo = new Object[]{messageText, messageIcon, username, userIcon};

        mainView.addMessageRow(messageInfo);
    }

    public void addContact(String username) {
        for (User user : onlineUsers.getUserList()) {
            if (user.getUserName().equals(username)) {
                clientContacts.addContact(user);
                return;
            }
        }
    }

    public void removeContact(String username) {
        clientContacts.removeContact(username);
    }

    public void addReceiver(String username) {
        for(User user : onlineUsers.getUserList()) {
            if(user.getUserName().equals(username)) {
                receivers.addReceiver(user);
                return;
            }
        }
    }

    public void removeReceiver(String username) {
        receivers.removeReceiver(username);
    }

    public void updateOnlineUsersGUI() {
        List<User> userList = onlineUsers.getUserList();
        Object[][] userInfo = new Object[userList.size()][4];
        for (int i = 0; i < userInfo.length; i++) {
            for (int j = 0; j < userInfo[0].length; j++) {
                if(j == 0) {
                    userInfo[i][j] = userList.get(i).getUserIcon();
                } else if (j == 1) {
                    userInfo[i][j] = userList.get(i).getUserName();
                } else if (j == 2) {
                    //create button from mainframe
                } else if (j == 3) {
                    //create button from mainframe
                }
            }
        }
        mainView.updateOnlineUsersGUI(userInfo);
    }
}
