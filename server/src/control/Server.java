package control;

import entity.*;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private ServerSocket serverSocket;
    private ActiveClients activeClients;
    private RegisteredUsers registeredUsers;
    private UnsentMessages unsentMessages;

    public Server(ServerSocket serverSocket, RegisteredUsers registeredUsers) {
        activeClients = ActiveClients.getInstance();
        //unsentMessages.getInstance(); //TODO
        registeredUsers = registeredUsers; //TODO read from file instead
    }

    public void connectClient(User user) {
        new ClientHandler(user).start();
    }

    //TODO doesn't work yet
    public void sendMessageToHandler(Message message) {
        for (User user : message.getReceivers()) {
            ClientConnection clientConnection = activeClients.get(user);
            if(clientConnection != null) {
                clientConnection.getThread().addMessageToHandlerList(message);
            } else {
                unsentMessages.put(user, message);
            }
        }
    }

    public class ClientHandler extends Thread {
        private User user;
        private ClientConnection clientConnection;
        private Socket clientSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private LinkedBlockingQueue<Message> messageList;

        public ClientHandler(User user) {
            this.user = user;
            this.clientConnection = activeClients.get(user);
            clientConnection.addThread(this);
            this.clientSocket = clientConnection.getSocket();
            this.oos = clientConnection.getOutputStream();
            this.ois = clientConnection.getInputStream();
            messageList = new LinkedBlockingQueue<Message>();
        }

        @Override
        public void run() {
            System.out.println("Server: Client Handler Started");

            try {
                while (!clientSocket.isClosed()) {
                    try {
                        System.out.println("Server: Trying to Read Object");
                        sendMessageToHandler((Message) ois.readObject()); //TODO not fully implemented
                        //System.out.println(message.getMessageText());
                        try {
                            oos.writeObject(messageList.take()); //TODO not fully implemented, might work?
                        } catch (InterruptedException ie) {
                            //thread waiting for Messages
                        }
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

        public void addMessageToHandlerList(Message message) {
            try {
                messageList.put(message); //TODO not fully implemented, might work?
            } catch (InterruptedException ie) {
                //thread waiting to put Messages
            }

        }
    }
}
