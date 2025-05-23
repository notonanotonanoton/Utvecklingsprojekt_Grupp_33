package control;

import boundary.ServerBoundary;
import entity.*;
import shared_entity.message.ExitMessage;
import shared_entity.message.Message;
import shared_entity.message.UsersOnlineMessage;
import shared_entity.user.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Server class represents the main server logic for handling client connections
 * and message communication.
 */
public class Server implements PropertyChangeListener {
    private ClientConnectionList clientConnectionList;
    private UnsentMessages unsentMessages;

    /**
     * Constructor for the Server class.
     */
    public Server() {
        clientConnectionList = ClientConnectionList.getInstance();
        clientConnectionList.addPropertyChangeListener(this);
        unsentMessages = UnsentMessages.getInstance();
    }

    /**
     * Method to connect a client to the server.
     * @param user The User object representing the client.
     * @param clientConnection The ClientConnection object representing the client's connection.
     */
    public synchronized void connectClient(User user, ClientConnection clientConnection) {
        new ClientHandler(user, clientConnection).start();
    }

    //sends update to all connected clients about the online/offline status of users
    @Override
    public synchronized void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals("clients")) {
            System.out.println("Got ClientConnectionList update, sending Message");
            UsersOnlineMessage onlineMessage = new UsersOnlineMessage(clientConnectionList.getAllUsers());
            System.out.println(clientConnectionList.getAllUsers());
            sendMessageToHandler(onlineMessage);
        }
    }

    /**
     * Method to send a message to the appropriate client handler.
     * @param messageObject The message to be sent.
     */
    public synchronized void sendMessageToHandler(Object messageObject) {
        Message message = (Message) messageObject;
        message.setReceivedByServer();
        System.out.println("send to message handler received");

        if (message instanceof ExitMessage) {
            System.out.println("Server: Exit Message Received");
            ExitMessage exitMessage = (ExitMessage) message;
            clientConnectionList.removeClient(exitMessage.getSender());
            return;
        }

        for (User user : message.getReceivers()) {
            ClientConnection clientConnection = clientConnectionList.get(user);
            if (clientConnection != null) {
                clientConnection.getThread().addMessageToHandlerList(message);
            } else {
                unsentMessages.put(user, message);
                System.out.println("MESSAGE TO " + user + " PUT IN UNSENTMESSAGES!!!! \n");
            }
        }
    }

    /**
     * Inner class representing a client handler thread.
     */
    public class ClientHandler extends Thread {
        private User user;
        private Socket clientSocket;
        private LinkedBlockingQueue<Message> messageList;
        private ServerBoundary serverBoundary;
        private ActivityFileLogger logger;

        /**
         * Constructor for ClientHandler.
         * @param user The User object associated with this handler.
         * @param clientConnection The ClientConnection object for this handler.
         */
        public ClientHandler(User user, ClientConnection clientConnection) {
            this.user = user;
            clientConnection.addThread(this);
            System.out.println("Added user '" + user.getUserName() + "' and connection to ClientConnectionList");
            this.clientSocket = clientConnection.getSocket();
            this.serverBoundary = new ServerBoundary(Server.this, clientSocket,
                    clientConnection.getOutputStream(), clientConnection.getInputStream());
            messageList = new LinkedBlockingQueue<>();
            loadUnsentMessages();
            logger = new ActivityFileLogger();
            clientConnectionList.put(user, clientConnection);
        }

        /**
         * Main execution logic for the client handler thread.
         */
        @Override
        public void run() {
            System.out.println("Server: Client Handler Started");
            while (!clientSocket.isClosed()) {
                try {
                    Message message = messageList.take();
                    serverBoundary.writeMessageToClient(message);
                    logger.logInfo(message.toString(), LocalDateTime.now());
                } catch (InterruptedException ie) {
                    System.out.println("Server: Interrupted Take");
                }
            }
            System.out.println("Server: Client Socket Closed");
        }

        /**
         * Method to add a message to the handler's message list.
         * @param message The message to be added.
         */
        public void addMessageToHandlerList(Message message) {
            try {
                messageList.put(message);
            } catch (InterruptedException ie) {
                System.out.println("Server: Interrupted Put");
            }
        }

        /**
         * Method to load unsent messages for the user associated with this handler.
         */
        private void loadUnsentMessages() {
            LinkedList<Message> userMessages = unsentMessages.get(user);
            if (userMessages != null) {
                for (Message message : userMessages) {
                    addMessageToHandlerList(message);
                }
                unsentMessages.remove(user);
            }
        }
    }
}