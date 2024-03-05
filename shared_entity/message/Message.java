package shared_entity.message;

import shared_entity.user.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {
    private User sender;
    private List<User> receivers;
    private String messageText;
    private ImageIcon messageImage;
    private Date receivedByServer;
    private Date receivedByUser;

    public Message() {

    }
    public Message(String messageText) {
        this();
        this.messageText = messageText;
    }
    public Message(ImageIcon messageImage) {
        this();
        this.messageImage = messageImage;}
    public Message(String messageText, ImageIcon messageImage) {
        this();
        this.messageText = messageText;
        this.messageImage = messageImage;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public List<User> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<User> receivers) {
        this.receivers = receivers;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public ImageIcon getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(ImageIcon messageImage) {
        this.messageImage = messageImage;
    }

    @Override
    public String toString() {
        return "User " + sender + " sent a message to: " + getReceivers().toString();
    }
}
