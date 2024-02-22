package shared_entity.message.message_state;

import shared_entity.message.Message;

public class MessageSending implements MessageState{
    @Override
    public void next(Message message) {
        message.setState(new MessageSent());
    }

    @Override
    public void prev(Message message) {
        message.setState(new MessageWaiting());
    }

    @Override
    public void printStatus() {
        System.out.println("Message Status: " + this.getClass().getSimpleName());
    }
}
