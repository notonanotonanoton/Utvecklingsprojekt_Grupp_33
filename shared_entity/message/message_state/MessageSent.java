package shared_entity.message.message_state;

import shared_entity.message.Message;

public class MessageSent implements MessageState{
    @Override
    public void next(Message message) {
        System.out.println("End state, can't go forward");
    }

    @Override
    public void prev(Message message) {
        message.setState(new MessageSending());
    }

    @Override
    public void printStatus() {
        System.out.println("Message Status: " + this.getClass().getSimpleName());
    }
}
