package control;

import boundary.ClientMainView;
import entity.OnlineUsers;
import shared_entity.message.Message;
import shared_entity.message.UserMessage;
import shared_entity.message.UsersOnlineMessage;
import shared_entity.user.User;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private User user;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ClientMainView mainView;
    private OnlineUsers onlineUsers;

    public Client(User user, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.user = user;
        this.clientSocket = clientSocket;
        this.oos = oos;
        mainView = new ClientMainView(this, oos, ois);
        onlineUsers = new OnlineUsers();
    }

    //TODO add full implementation when possible
    public void handleMessage(Message message) {
        if (message instanceof UsersOnlineMessage) {
            onlineUsers = new OnlineUsers(message.getReceivers());
        } else {
            mainView.readMessageFromServer(message);
        }
    }

    public void assembleUserToUserMessage(String messageText, ImageIcon messageImage) {
        UserMessage message = new UserMessage();
        if(messageText != null) {
            message.setMessageText(messageText);
        }
        if(messageImage != null) {
            message.setMessageImage(messageImage);
        }
        message.setSender(user);
        //TODO get receivers from GUI
        message.setReceivers(onlineUsers.getUserList());
        //new MessageToServer(message, oos);
        mainView.sendMessageToServer(message);
        System.out.println("Message '" + message.getMessageText() + "' sent to server");
    }
}
