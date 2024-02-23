package shared_entity.message;

import shared_entity.message.message_state.MessageState;
import shared_entity.message.message_state.MessageWaiting;
import shared_entity.user.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private MessageState messageState;
    private User sender;
    private User[] receivers;
    private String messageText;
    private ImageIcon messageImage;
    private Date receivedByServer;
    private Date sentToClient;

    public Message() {
        messageState = new MessageWaiting();
    }
    public Message(String messageText) {
        this.messageText = messageText;
    }

    public void setState(MessageState messageState) {
        this.messageState = messageState;
    }

    public void nextState() {
        messageState.next(this);
    }

    public void previousState() {
        messageState.prev(this);
    }

    public void printStatus() {
        messageState.printStatus();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
