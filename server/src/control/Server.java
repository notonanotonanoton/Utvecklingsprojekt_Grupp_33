package control;

import entity.ClientConnection;
import entity.ActiveClients;
import entity.RegisteredUsers;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private ActiveClients activeClients;
    private RegisteredUsers registeredUsers;

    public Server(ServerSocket serverSocket, RegisteredUsers registeredUsers) {
        activeClients = ActiveClients.getInstance();
        registeredUsers = registeredUsers; //TODO read from file instead
    }

    public void connectClient(User user) {
        new ClientHandler(user).start();
    }

    private class ClientHandler extends Thread {
        User user;
        ClientConnection clientConnection;
        Socket clientSocket;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        List<Message> messageList;

        private ClientHandler(User user) {
            this.user = user;
            this.clientConnection = activeClients.get(user);
            this.clientSocket = clientConnection.getSocket();
            this.oos = clientConnection.getOutputStream();
            this.ois = clientConnection.getInputStream();
            messageList = new LinkedList<>();
        }

        @Override
        public void run() {
            System.out.println("Server: Client Handler Started");
            try {
                while (!clientSocket.isClosed()) {
                    try {
                        System.out.println("Server: Trying to Read Object");
                        Message message = (Message) ois.readObject();
                        System.out.println(message.getMessageText());
                        oos.writeObject(message);
                        oos.flush();
                    } catch (ClassNotFoundException cnfe) {
                        System.out.println("Server: Message Type Mismatch");
                    }
                }
                System.out.println("Server: Client Socket Closed");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Server: IO Exception");
            }

        }
    }
}
