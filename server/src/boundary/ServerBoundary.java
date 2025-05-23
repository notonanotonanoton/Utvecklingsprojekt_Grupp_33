package boundary;

import control.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The ServerBoundary class handles communication between the server and a connected client.
 * It reads incoming messages from the client and writes outgoing messages to the client.
 */
public class ServerBoundary extends Thread {
    private Server server;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Constructor for the ServerBoundary class.
     * @param server The Server object associated with this boundary.
     * @param clientSocket The client's socket connection.
     * @param oos The ObjectOutputStream for writing objects to the client.
     * @param ois The ObjectInputStream for reading objects from the client.
     */
    public ServerBoundary(Server server, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.oos = oos;
        this.ois = ois;
        start();
    }

    /**
     * Main execution logic for the ServerBoundary thread.
     */
    @Override
    public void run() {
        System.out.println("Server Boundary: Started");
        try {
            while (!clientSocket.isClosed()) {
                try {
                    System.out.println("Server Boundary: Trying to Read Object");
                    server.sendMessageToHandler(ois.readObject());
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("Server: Message Type Mismatch");
                } catch (EOFException eofe) {
                    System.out.println("Server Boundary: Client Disconnected");
                    break;
                }
            }
            System.out.println("Server Boundary: Client Handler Thread Closed");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Boundary: IO Exception");
        }
    }

    /**
     * Method to write a message to the connected client.
     * @param messageObject The message object to be written.
     */
    public synchronized void writeMessageToClient(Object messageObject) {
        try {
            System.out.println("writing message to client");
            oos.writeObject(messageObject);
            oos.flush();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("Server Boundary: Write Message Error");
        }
    }
}
