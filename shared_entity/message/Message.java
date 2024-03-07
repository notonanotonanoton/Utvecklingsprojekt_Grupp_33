package shared_entity.message;

import shared_entity.user.User;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Message implements Serializable {
    private User sender;
    private List<User> receivers;
    private String messageText;
    private ImageIcon messageIcon;
    private LocalDateTime receivedByServer;
    private LocalDateTime recievedByUser;

    public List<User> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<User> receivers) {
        this.receivers = receivers;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public ImageIcon getMessageImage() {
        return messageIcon;
    }

    public void setMessageImage(ImageIcon messageIcon) {
        this.messageIcon = messageIcon;
    }

    public LocalDateTime receivedByServer() {
        return this.receivedByServer;
    }

    public LocalDateTime receivedByUser() {
        return this.recievedByUser;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User user) {
        this.sender = user;
    }

    @Override
    public String toString() {
        return "Sent Message from: " + getSender() + ", to: " + getReceivers();
    }
}