import boundary.ActionLogView;
import control.LoginHandler;
import control.Server;
import entity.RegisteredUsers;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3343); //TODO there may be a better way to do this
        } catch (IOException ioe) {
            System.out.println("ServerMain: Port Error");
        }
        Server server = new Server(serverSocket);
        ActionLogView actionLogView = new ActionLogView();
        new LoginHandler(serverSocket, server, RegisteredUsers.getInstance());
    }
}
