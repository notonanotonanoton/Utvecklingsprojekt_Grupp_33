import boundary.ActionLogView;
import control.LoginHandler;
import control.Server;
import entity.RegisteredUsers;

import java.io.IOException;
import java.net.ServerSocket;

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
