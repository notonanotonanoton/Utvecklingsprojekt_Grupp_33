package control;

import boundary.ServerBoundary;
import entity.ClientConnection;
import entity.ClientConnectionList;
import entity.RegisteredUsers;
import entity.UnsentMessages;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private ServerSocket serverSocket;
    private ClientConnectionList clientConnectionList;
    private RegisteredUsers registeredUsers;
    private UnsentMessages unsentMessages;

    public Server(ServerSocket serverSocket, RegisteredUsers registeredUsers) {
        clientConnectionList = ClientConnectionList.getInstance();
        //unsentMessages = unsentMessages.getInstance(); //TODO
        registeredUsers = registeredUsers; //TODO read from file instead
    }

    public void connectClient(User user, ClientConnection clientConnection) {
        new ClientHandler(user, clientConnection).start();
    }

    //TODO doesn't work yet
    public void sendMessageToHandler(Message message) {
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
            this.clientSocket = clientConnection.getSocket();
            this.serverBoundary = new ServerBoundary(Server.this,
                    clientConnection.getOutputStream(), clientConnection.getInputStream());
            messageList = new LinkedBlockingQueue<Message>();
        }

        @Override
        public void run() {
            System.out.println("Server: Client Handler Started");
            while (!clientSocket.isClosed()) {
                try {
                    serverBoundary.writeMessageToClient(messageList.take());
                } catch (InterruptedException ie) {
                    //waiting for messageList
                }
            }
            System.out.println("Server: Client Socket Closed");
        }

        public void addMessageToHandlerList(Message message) {
            try {
                messageList.put(message);
            } catch (InterruptedException ie) {
                //thread waiting to put Messages
            }

        }

        //TODO solution to fix redundancy?
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
