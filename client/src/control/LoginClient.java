package control;

import boundary.LoginView;
import shared_entity.message.LoginMessage;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.*;
import java.net.Socket;

public class LoginClient {
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private LoginView loginView;

    public LoginClient(String ip, int port) {
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
                System.out.println("waiting for response");
                response = loginView.readResponseFromServer();
                System.out.println("Server Response: " + response);
            }

            if (response == 12) {
                loginView.selectProfilePicture();
            }

            loginView.closeLogin();
            if (!clientSocket.isClosed()) {
                try {
                    Message message = (Message)loginView.getUserMessageFromServer();
                    if (!(message instanceof LoginMessage)) {
                        System.out.println("Login Client: Login Aborted, Wrong Message Type");
                        return;
                    }
                    User user = message.getSender();
                    System.out.println("Received user '" + user.getUserName() + "' from server");
                    new Client(user, clientSocket, oos, ois);

                } catch (ClassNotFoundException cfe) {
                    cfe.printStackTrace();
                    System.out.println("Login Client: Read Object Class Mismatch");
                    return;
                }

            } else {
                System.out.println("Login Client: Login Aborted, Socket is Closed");
            }
            System.out.println("Login Finished, closing...");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Login Client: Login Error");
        }
    }
}