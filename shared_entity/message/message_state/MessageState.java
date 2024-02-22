package shared_entity.message.message_state;

import shared_entity.message.Message;

import java.io.Serializable;

public interface MessageState extends Serializable {

    void next(Message message);
    void prev(Message message);
    void printStatus();
}
