import control.LoginHandler;
import control.Server;
import entity.RegisteredUsers;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3343); //TODO maybe better way to do this
        } catch (IOException ioe) {
            System.out.println("ServerMain: Port Error");
        }
        RegisteredUsers registeredUsers = new RegisteredUsers(); //TODO do not create new, read from file instead
        Server server = new Server(serverSocket, registeredUsers);
        new LoginHandler(serverSocket, server, registeredUsers);
    }
}
