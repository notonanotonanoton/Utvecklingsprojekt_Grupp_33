package boundary;

import shared_entity.message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageToServer extends Thread {
    private Message message;
    private ObjectOutputStream oos;
    public MessageToServer(Message message, ObjectOutputStream oos) {
        this.message = message;
        this.oos = oos;
        start();
    }

    @Override
    public void run() {
        try {
            oos.writeObject(message);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Message to Server: Write Error");
        }
    }
}
