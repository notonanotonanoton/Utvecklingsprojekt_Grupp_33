package boundary;

import control.Server;
import shared_entity.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerBoundary extends Thread {
    private Server server;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ServerBoundary(Server server, ObjectOutputStream oos, ObjectInputStream ois) {
        this.server = server;
        this.oos = oos;
        this.ois = ois;
        start();
    }

    @Override
    public void run() {
        System.out.println("Server Boundary: Started");
        try {
            while (Server.ClientHandler.currentThread().isAlive()) {
                try {
                    System.out.println("Server Boundary: Trying to Read Object");
                    server.sendMessageToHandler((Message) ois.readObject());
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("Server: Message Type Mismatch");
                }
            }
            System.out.println("Server Boundary: Client Handler Thread Closed");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Boundary: IO Exception");
        }
    }

    public void addMessageToHandlerList(Message message) {
        server.sendMessageToHandler(message);

    }

    public void writeMessageToClient(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("Server Boundary: Write Message Error");
        }

    }
}
