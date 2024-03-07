package control;

import boundary.LoginView;
import shared_entity.message.LoginMessage;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.*;
import java.net.Socket;

//TODO should not extend Thread!!! only for message send test!!!
public class LoginClient extends Thread {
    private String userName;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private LoginView loginView;

    //TODO remove tempUser parameter (& instance variable) and enterUsername after adding real LoginClient implementation
    public LoginClient(String ip, int port, String tempUser) {

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
                System.out.println("waiting for response");
                response = loginView.readResponseFromServer();
                System.out.println("Server Response: " + response);
            }
            loginView.closeLoginWindow();
            if (!clientSocket.isClosed()) {
                try {
                    Message message = loginView.getUserMessageFromServer();
                    if (!(message instanceof LoginMessage)) {
                        System.out.println("Login Client: Login Aborted, Wrong Message Type");
                        return;
                    }
                    User user = message.getSender();
                    System.out.println("Received user '" + user.getUserName() + "' from server");
                    Client client = new Client(user, clientSocket, oos, ois);

                    //TODO timing issue that can possibly be fixed? assembleMessage doesn't work if immediately called
                    try {
                        sleep(3000);
                    } catch (InterruptedException ie) {
                        //hhhh
                    }

                    //TODO remove this test later
                    client.assembleUserToUserMessage("Hej hej!", null);

                } catch (ClassNotFoundException cfe) {
                    cfe.printStackTrace();
                    System.out.println("Client: Read Object Class Mismatch");
                    return;
                }

            } else {
                System.out.println("Login Client: Login Aborted, Socket is Closed");
            }
            System.out.println("Login Finished");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client: Login Error");
        }
    }
}