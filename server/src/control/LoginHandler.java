package control;

import boundary.LoginBoundary;
import entity.ClientConnection;
import entity.ClientConnectionList;
import entity.RegisteredUsers;
import shared_entity.user.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginHandler extends Thread {
    private ServerSocket serverSocket;
    private RegisteredUsers registeredUsers;
    private ClientConnectionList clientConnectionList;
    private Server server;

    public LoginHandler(ServerSocket serverSocket, Server server, RegisteredUsers registeredUsers) {
        this.serverSocket = serverSocket;
        this.registeredUsers = registeredUsers; //TODO read from file instead
        clientConnectionList = ClientConnectionList.getInstance();
        this.server = server;
        start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                new ClientLogin(serverSocket.accept());
                System.out.println("Server connected with client");
            } catch (IOException ioe) {
                System.out.println("Server: Client Socket Error");
            }
        }
    }


    public class ClientLogin {
        private User user;
        private ClientConnection clientConnection;
        private LoginBoundary loginBoundary;

        public ClientLogin(Socket clientSocket) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                oos.flush(); //required because of buffer
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                clientConnection = new ClientConnection(clientSocket, oos, ois);
                loginBoundary = new LoginBoundary(this, oos, ois);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Login Server: Initialization Error");
            }
            loginUser();
        }

        public void loginUser() {
            int responseToClient = 10;
            String username = loginBoundary.readUsernameFromClient();
            System.out.println("Username: " + username + " was put in");
            while (username.length() <3 && username.length() >18 && !username.matches("^[a-zA-Z0-9]+")) {
                System.out.println("Invalid username");
                loginBoundary.writeResponseToClient(responseToClient);
                username = loginBoundary.readUsernameFromClient();
            }
            user = registeredUsers.findUser(username);
            if (user != null) {
                responseToClient = 11;
                System.out.println("Logging in to user: " + username);
            } else {
                responseToClient = 12;
                System.out.println("Creating new user: " + username);
                user = new User(username);
                registeredUsers.addUser(user);
            }
            clientConnectionList.put(user, clientConnection);
            server.connectClient(user, clientConnection);
            System.out.println("Connected client to main server");
            loginBoundary.writeResponseToClient(responseToClient);
            System.out.println("Added user and client to ClientConnectionList");
        }
    }
}
