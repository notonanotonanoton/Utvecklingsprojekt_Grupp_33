import control.LoginClient;
import control.LoginHandler;
import entity.RegisteredUsers;
import control.Server;

import java.io.IOException;
import java.net.ServerSocket;


public class ClientTest {

    public void testClientServerCommunication() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3343); //TODO there may be a better way to do this
        } catch (IOException ioe) {
            System.out.println("ServerMain: Port Error");
        }
        Server server = new Server(serverSocket);
        new LoginHandler(serverSocket, server, RegisteredUsers.getInstance());
        //try {
        //Thread.sleep(1000);
        //} catch (InterruptedException e) {
        //throw new RuntimeException(e);
        //}

        LoginClient client = new LoginClient("127.0.0.1", 3343, "Orvar");
        LoginClient client2 = new LoginClient("127.0.0.1", 3343, "GroundZeroGreta");
        LoginClient client3 = new LoginClient("127.0.0.1", 3343, "Manne");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException IE) {

        }
        // registeredUsers.saveUsersToFile();
    }
}