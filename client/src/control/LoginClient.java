package control;

import java.io.*;
import java.net.Socket;

public class LoginClient {
    private String ip;
    private int port;
    private String userName;
    private Socket clientSocket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    //TODO remove tempUser parameter
    public LoginClient(String ip, int port, String tempUser) {
        this.ip = ip;
        this.port = port;
        this.userName = tempUser;
        login();
    }

    public void login() {
        String username = enterDetails();
        connectToServer();
        try {
            oos.writeUTF(username);
            oos.flush();
            int response = ois.readInt();
            System.out.println("Server Response: " + response);
            if (response >= 10) {
                new Client(clientSocket, oos, ois);
            } else {
                System.out.println("Client: Code Mismatch");
            }
            System.out.println("Login Finished");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client: Login Error");
        }
    }

    private void connectToServer(){
        try {
            clientSocket = new Socket(ip, port);
            oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            oos.flush(); //required because of buffer
            ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Client: Server Connection Failed");
        }
    }

    //TODO add real implementation
    private String enterDetails() {
        return userName;
    }

}