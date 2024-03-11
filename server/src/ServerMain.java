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
        try {
            ServerSocket serverSocket = new ServerSocket(3343);
            Server server = new Server();
            LoginHandler loginHandler = new LoginHandler(serverSocket, server, RegisteredUsers.getInstance());
            ActionLogView actionLogView = new ActionLogView();
        } catch (IOException ioe) {
            System.out.println("Server Main: Port Error");
        }
    }
}
