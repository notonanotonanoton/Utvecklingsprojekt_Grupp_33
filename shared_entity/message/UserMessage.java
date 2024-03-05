package shared_entity.message;

import shared_entity.user.User;

import javax.swing.*;

public class UserMessage extends Message {

    private User sender;
    private ImageIcon messageImage;

    public void setMessageImage(ImageIcon messageImage) {
        this.messageImage = messageImage;
    }

    public void setSender(User user) {
        this.sender = user;
    }

    @Override
    public String toString() {
        return "Sent UserMessage from " + sender + " to " + super.getReceivers();
    }
}
