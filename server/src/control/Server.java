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
        //unsentMessage = unsentMessages.getInstance(); //TODO
        registeredUsers = registeredUsers; //TODO read from file instead
    }

    public void connectClient(User user) {
        new ClientHandler(user).start();
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
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private LinkedBlockingQueue<Message> messageList;
        private ServerBoundary serverBoundary;

        public ClientHandler(User user) {
            this.user = user;
            this.clientConnection = clientConnectionList.get(user);
            clientConnection.addThread(this);
            this.clientSocket = clientConnection.getSocket();
            this.oos = clientConnection.getOutputStream();
            this.ois = clientConnection.getInputStream();
            this.serverBoundary = new ServerBoundary(Server.this, oos, ois);
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
                messageList.put(message); //TODO not fully implemented, might work?
            } catch (InterruptedException ie) {
                //thread waiting to put Messages
            }

        }

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
