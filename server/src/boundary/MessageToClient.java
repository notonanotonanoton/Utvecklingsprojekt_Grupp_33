package boundary;

import shared_entity.message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageToClient extends Thread {
    private Message message;
    private ObjectOutputStream oos;

    public MessageToClient(Message message, ObjectOutputStream oos) {
        this.message = message;
        this.oos = oos;
        start();
    }

    @Override
    public void run() {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("Server Boundary: Write Message Error");
        }
    }
}