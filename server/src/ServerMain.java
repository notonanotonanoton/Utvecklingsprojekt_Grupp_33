import boundary.ActionLogView;
import control.LoginHandler;
import control.Server;
import entity.RegisteredUsers;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The ServerMain class contains the main method to start the server application.
 * It initializes the server socket, creates instances of the server, action log view, and login handler.
 */
public class ServerMain {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3343);
        } catch (IOException ioe) {
            System.out.println("ServerMain: Port Error");
        }
        Server server = new Server(serverSocket);
        ActionLogView actionLogView = new ActionLogView();
        new LoginHandler(serverSocket, server, RegisteredUsers.getInstance());
    }
}
