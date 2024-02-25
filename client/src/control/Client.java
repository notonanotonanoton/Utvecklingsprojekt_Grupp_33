package control;

import shared_entity.message.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private String ip;
    private int port;
    private int loginPort;
    private String userName;
    private Socket clientSocket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    //TODO remove tempUsers
    public Client(String ip, int port, int loginPort, String tempUser) {
        this.ip = ip;
        this.port = port;
        this.loginPort = loginPort;
        this.userName = tempUser;
        LoginClient loginClient = new LoginClient();
        loginClient.start();
        try {
            loginClient.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void establishConnectionToServer(int port) {
        try {
            clientSocket = new Socket(ip, port);
        } catch (IOException ioe) {
            System.out.println("Client: Host Error");
        }
    }

    public void sendMessageToServer(String messageBody, ImageIcon messageIcon) {
        System.out.println("Message Text: " + messageBody);
        Message message = new Message(messageBody);
        try {
            oos.writeObject(message);
            oos.flush();
            System.out.println("Client: Message Sent: " + message.getMessageText());
        } catch (IOException ioe) {
            System.out.println("Client: Message Error: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    private class ActiveClient extends Thread {

        @Override
        public void run() {
            establishConnectionToServer(port);
            System.out.println("Client active");
            try {
                oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                oos.flush(); //required because of buffer
                ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                System.out.println("Client active, test sending message");
                //sendMessageToServer("Hej hej!", null); //TODO remove
                while (!clientSocket.isClosed()) {
                    //TODO
                    try {
                        Message message = ((Message) ois.readObject());
                        System.out.println(message.getMessageText());
                    } catch (ClassNotFoundException cnfe) {
                        System.out.println("Client: Message Type Mismatch");
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Client: Error");
            }
        }
    }

    private class LoginClient extends Thread {
        @Override
        public void run() {
            establishConnectionToServer(loginPort);
            try (DataInputStream dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                 DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
                dos.writeUTF(userName);
                dos.flush();
                int response = dis.readInt();
                System.out.println("Server Response: " + response);
                if (response >= 10) {
                    clientSocket.close();
                    new ActiveClient().start();
                } else {
                    System.out.println("Client: Code Mismatch");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Client: Login Error");
            }
        }
    }
}
