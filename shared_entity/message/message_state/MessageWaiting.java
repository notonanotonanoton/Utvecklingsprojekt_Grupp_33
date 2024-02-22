package shared_entity.message.message_state;

import shared_entity.message.Message;

public class MessageWaiting implements MessageState {
    @Override
    public void next(Message message) {
        message.setState(new MessageSending());
    }

    @Override
    public void prev(Message message) {
        System.out.println("Base state, can't go back");
    }

    @Override
    public void printStatus() {
        System.out.println("Message Status: " + this.getClass().getSimpleName());
    }
}
