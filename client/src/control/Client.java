package control;

import boundary.ClientMainView;
import entity.OnlineUsers;
import shared_entity.message.Message;
import shared_entity.message.UsersOnlineMessage;
import shared_entity.user.User;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private User user;
    private Socket clientSocket;
    private ClientMainView mainView;
    private OnlineUsers onlineUsers;

    public Client(User user, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.user = user;
        this.clientSocket = clientSocket;
        mainView = new ClientMainView(this, oos, ois);
        onlineUsers = new OnlineUsers();
    }

    //TODO add full implementation when possible
    public void handleMessage(Object messageObject) {
        Message message = (Message) messageObject;
        message.setReceivedByUser(); // correct?
        if (message instanceof UsersOnlineMessage) {
            onlineUsers = new OnlineUsers(message.getReceivers());
        }
        else {
            displayMessageFromServer(message);
        }
    }

    public void assembleUserToUserMessage(String messageText, ImageIcon messageImage) {
        Message message = new Message();
        if(messageText != null) {
            message.setMessageText(messageText);
        }
        if(messageImage != null) {
            message.setMessageImage(messageImage);
        }
        message.setSender(user);
        //TODO get receivers from GUI
        message.setReceivers(onlineUsers.getUserList());
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
        if(messageText == null) {
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

        Object[] messageInfo = new Object[]{messageText, messageIcon, formattedUsername, userIcon};

        mainView.addMessageRow(messageInfo);
    }
}
