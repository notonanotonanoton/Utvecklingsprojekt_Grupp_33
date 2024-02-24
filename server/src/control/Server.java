package control;

import entity.Client;
import entity.Clients;
import entity.RegisteredUsers;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverLoginSocket;
    private ServerSocket serverSocket;
    private Clients activeClients;
    private RegisteredUsers registeredUsers;

    public Server(int port, int loginPort) {
        try {
            serverSocket = new ServerSocket(port);
            serverLoginSocket = new ServerSocket(loginPort);
        } catch (IOException ioe) {
            System.out.println("Server: Port Error");
        }
        activeClients = new Clients();
        registeredUsers = new RegisteredUsers(); //TODO do not create new, read from file instead
        start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                new ClientLogin(new Client(serverLoginSocket.accept())).start();
                System.out.println("Server connected with client");
            } catch (IOException ioe) {
                System.out.println("Server: Client Socket Error");
            }
        }
    }

    //TODO functionality undefined
    private class ClientHandler extends Thread {
        Client client;
        User user;
        List<Message> messageList;

        private ClientHandler(Client client, User user) {
            this.client = client;
            this.user = user;
            messageList = new LinkedList<>();
        }

        @Override
        public void run() {
            Socket clientSocket = client.getSocket();
            System.out.println("Server: Client Handler Started");
            System.out.println("Server: Is Socket Closed?: " + clientSocket.isClosed());
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                 ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
                System.out.println("Server: Is Socket Closed?: " + clientSocket.isClosed());
                while (!clientSocket.isClosed()) {
                    try {
                        System.out.println("Server: Trying to Read Object");
                        Message message = (Message)ois.readObject();
                        System.out.println(message.getMessageText());
                    } catch (ClassNotFoundException cnfe) {
                        System.out.println("Server: Message Type Mismatch");
                    }


                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Server: IO Exception");
            }

        }
    }

    private class ClientLogin extends Thread {
        Client client;

        private ClientLogin(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            Socket clientSocket = client.getSocket();
            try (DataInputStream dsr = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                 DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
                System.out.println("Awaiting String read");
                String username = dsr.readUTF();
                System.out.println("Read String: " + username);
                User user = registeredUsers.findUser(username);
                int responseToClient = 10;
                if (user != null) {
                    responseToClient = 11;
                    System.out.println("Logging in to user: " + username);
                } else {
                    System.out.println("Creating new user: " + username);
                    registeredUsers.addUser(new User(username));
                }
                dos.writeInt(responseToClient);
                dos.flush();
                System.out.println("Wrote response to client");
                new ClientHandler(new Client(serverSocket.accept()), user).start();
                System.out.println("Established new connection");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Server: IO Exception");
            }
        }
    }
}
