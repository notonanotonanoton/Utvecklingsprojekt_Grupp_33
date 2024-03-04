package control;

import boundary.ServerBoundary;
import entity.ClientConnection;
import entity.ClientConnectionList;
import entity.RegisteredUsers;
import entity.UnsentMessages;
import shared_entity.message.Message;
import shared_entity.message.UsersOnlineMessage;
import shared_entity.user.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Server implements PropertyChangeListener {
    private ServerSocket serverSocket;
    private ClientConnectionList clientConnectionList;
    private RegisteredUsers registeredUsers;
    private UnsentMessages unsentMessages;

    public Server(ServerSocket serverSocket, RegisteredUsers registeredUsers) {
        this.serverSocket = serverSocket;
        clientConnectionList = ClientConnectionList.getInstance();
        clientConnectionList.addPropertyChangeListener(this);
        //unsentMessages = unsentMessages.getInstance(); //TODO
        this.registeredUsers = registeredUsers; //TODO read from file instead
    }

    public synchronized void connectClient(User user, ClientConnection clientConnection) {
        new ClientHandler(user, clientConnection).start();
    }

    //sends update to all connected clients about the online/offline status of users
    @Override
    public synchronized void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals("clients")) {
            System.out.println("Got ClientConnectionList update, sending Message");
            UsersOnlineMessage onlineMessage = new UsersOnlineMessage(clientConnectionList.getAllUsers());
            sendMessageToHandler(onlineMessage);
        }
    }

    public synchronized void sendMessageToHandler(Message message) {
        System.out.println("send to message handler received");
        for (User user : message.getReceivers()) {
            ClientConnection clientConnection = clientConnectionList.get(user);
            if (clientConnection != null) {
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
        private LinkedBlockingQueue<Message> messageList;
        private ServerBoundary serverBoundary;

        public ClientHandler(User user, ClientConnection clientConnection) {
            this.user = user;
            this.clientConnection = clientConnection;
            clientConnection.addThread(this);
            System.out.println("Added user '" + user.getUserName() + "' and connection to ClientConnectionList");
            this.clientSocket = clientConnection.getSocket();
            this.serverBoundary = new ServerBoundary(Server.this,
                    clientConnection.getOutputStream(), clientConnection.getInputStream());
            messageList = new LinkedBlockingQueue<>();
            //careful of timing, ClientConnectionList has listener that sends Message
            clientConnectionList.put(user, clientConnection);
        }

        @Override
        public void run() {
            System.out.println("Server: Client Handler Started");
            while (!clientSocket.isClosed()) {
                try {
                    serverBoundary.writeMessageToClient(messageList.take());
                } catch (InterruptedException ie) {
                    //waiting for messageList
                    System.out.println("Server: Interrupted Take");
                }
            }
            System.out.println("Server: Client Socket Closed");
        }

        public void addMessageToHandlerList(Message message) {
            try {
                messageList.put(message);
            } catch (InterruptedException ie) {
                //thread waiting to put Messages
                System.out.println("Server: Interrupted Put");
            }
        }

        //TODO solution to fix redundancy? is this going to be used?
        public Message getMessageFromHandlerList() {
            Message message = new Message();
            try {
                message = (messageList.take());
            } catch (InterruptedException ie) {
                //thread waiting for Messages
            }
            return message;
        }
    }
}
