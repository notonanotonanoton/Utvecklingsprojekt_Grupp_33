package control;

import boundary.LoginView;

import java.io.*;
import java.net.Socket;

public class LoginClient {
    private String ip;
    private int port;
    private String userName;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private LoginView loginView;

    //TODO remove tempUser parameter (& instance variable) and enterUsername after adding real LoginClient implementation
    public LoginClient(String ip, int port, String tempUser) {
        this.ip = ip;
        this.port = port;
        this.userName = tempUser;
        try {
            clientSocket = new Socket(ip, port);
            oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            oos.flush(); //required because of buffer
            ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client: Server Connection Failed");
        }
        this.loginView = new LoginView(this, oos, ois);
        login();
    }

    public void login() {
        try {
            int response = -1;
            while(!(response >= 11)) {
                String username = loginView.enterUsername();
                loginView.sendUsernameToServer(username);
                System.out.println("waiting for response");
                response = loginView.readResponseFromServer();
                System.out.println("Server Response: " + response);
            }
            if (!clientSocket.isClosed()) {
                new Client(clientSocket, oos, ois);
            } else {
                System.out.println("Login Client: Login Aborted, Socket is Closed");
            }
            System.out.println("Login Finished");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client: Login Error");
        }
    }

    //TODO remove after adding real LoginView implementation
    public String enterUsername() {
        return userName;
    }

}