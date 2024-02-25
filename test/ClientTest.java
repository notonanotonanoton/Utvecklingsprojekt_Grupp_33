import control.LoginClient;
import control.LoginHandler;
import entity.RegisteredUsers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import control.Client;
import control.Server;

import java.io.IOException;
import java.net.ServerSocket;


public class ClientTest {

    @Test
    public void testClientServerCommunication() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3343); //TODO there may be a better way to do this
        } catch (IOException ioe) {
            System.out.println("ServerMain: Port Error");
        }
        RegisteredUsers registeredUsers = new RegisteredUsers(); //TODO do not create new, read from file instead
        Server server = new Server(serverSocket, registeredUsers);
        new LoginHandler(serverSocket, server, registeredUsers);
        //try {
            //Thread.sleep(1000);
        //} catch (InterruptedException e) {
            //throw new RuntimeException(e);
        //}

        LoginClient client = new LoginClient("127.0.0.1", 3343, "GroundZeroGreta");
    }
}