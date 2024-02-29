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


    public class ClientLogin extends Thread {
        Socket clientSocket;
        User user;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        ClientConnection clientConnection;
        LoginBoundary loginBoundary;

        public ClientLogin(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                oos.flush(); //required because of buffer
                ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                clientConnection = new ClientConnection(clientSocket, oos, ois);
                loginBoundary = new LoginBoundary(this, oos, ois);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Login Server: Initialization Error");
            }
        }

        @Override
        public void run() {
            while(!Thread.interrupted()) {
                //keep alive during login
            }
            System.out.println("Login Server: Login Thread Closing");
        }

        public void loginUser(String username) {
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
            clientConnectionList.put(user, clientConnection);
            loginBoundary.writeResponseToClient(responseToClient);
            System.out.println("Added user and client to ClientConnectionList");
            server.connectClient(user);
            interrupt();
        }
    }
}
