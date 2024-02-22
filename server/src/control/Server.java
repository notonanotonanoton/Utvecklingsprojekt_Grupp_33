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
    private ServerSocket serverSocket;
    private Clients activeClients;
    private RegisteredUsers registeredUsers;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
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
                new ClientLogin(new Client(serverSocket.accept())).start();
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
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(client.getSocket().getOutputStream()));
                 ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(client.getSocket().getInputStream()))) {
                while (!client.getSocket().isClosed()) {
                    oos.writeObject(messageList.getFirst());
                    oos.flush();
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
            boolean registering = true;
            try (DataInputStream dsr = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                 DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
                while (registering) {
                    String username = dsr.readUTF();
                    User user = registeredUsers.findUser(username);
                    String respondToClient = "Logging in to user: " + username;
                    if(user != null) {
                        new ClientHandler(client, user).start();
                    }
                    else {
                        respondToClient = "Creating new user: " + username;
                        registeredUsers.addUser(user);
                        new ClientHandler(client, user).start();
                    }
                    dos.writeUTF(respondToClient);
                    registering = false;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Server: IO Exception");
            }
        }
    }
}
