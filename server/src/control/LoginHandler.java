package control;

import entity.ClientConnection;
import entity.ActiveClients;
import entity.RegisteredUsers;
import shared_entity.user.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginHandler extends Thread {
    private ServerSocket serverSocket;
    private RegisteredUsers registeredUsers;
    private ActiveClients activeClients;
    private Server server;

    public LoginHandler(ServerSocket serverSocket, Server server, RegisteredUsers registeredUsers) {
        this.serverSocket = serverSocket;
        this.registeredUsers = registeredUsers; //TODO read from file instead
        activeClients = ActiveClients.getInstance();
        this.server = server;
        start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                new ClientLogin(serverSocket.accept()).start();
                System.out.println("Server connected with client");
            } catch (IOException ioe) {
                System.out.println("Server: Client Socket Error");
            }
        }
    }


    private class ClientLogin extends Thread {
        Socket clientSocket;
        User user;

        private ClientLogin(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }


        @Override
        public void run() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                oos.flush(); //required because of buffer
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                ClientConnection clientConnection = new ClientConnection(clientSocket, oos, ois);
                System.out.println("Awaiting String read");
                String username = ois.readUTF();
                System.out.println("Read String: " + username);
                user = registeredUsers.findUser(username);
                int responseToClient = 10;
                if (user != null) {
                    responseToClient = 11;
                    System.out.println("Logging in to user: " + username);
                } else {
                    System.out.println("Creating new user: " + username);
                    user = new User(username);
                    registeredUsers.addUser(user);
                }
                activeClients.put(user, clientConnection);
                System.out.println("Added user and client to ActiveClients");
                oos.writeInt(responseToClient);
                oos.flush();
                System.out.println("Wrote response to client");
                server.connectClient(user);
            } catch (IOException ioe) {
                activeClients.removeClient(user);
                ioe.printStackTrace();
                System.out.println("Server: IO Exception, Login Aborted");
            }
        }
    }
}
