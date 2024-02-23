package control;

import shared_entity.message.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    private String ip;
    private int port;
    private String userName;
    private static Socket clientSocket;

    //TODO remove tempUsers
    public Client(String ip, int port, String tempUser) {
        this.ip = ip;
        this.port = port;
        this.userName = tempUser;
        try{
            this.clientSocket = new Socket(ip, port);
        } catch (IOException ioe) {
            System.out.println("Client: Host Error");
        }
        new LoginClient().start();
    }

    public void sendMessageToServer(String messageBody, ImageIcon messageIcon) {
        System.out.println("1" + messageBody);
        Message message = new Message(messageBody);
        System.out.println("2" + message.getMessageText());
        new ClientMessage(message).start();
    }

    private class ActiveClient extends Thread {

        @Override
        public void run() {
            System.out.println("Client active");
            if(clientSocket != null) {
                System.out.println("Got socket!!!! Closed?: " + clientSocket.isClosed());
            } else {
                System.out.println("blegh");
            }
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
                System.out.println("Client active");
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
            System.out.println("hhhhhh");
            try (DataInputStream dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                 DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
                dos.writeUTF("GroundZeroGreta");
                dos.flush();
                int response = dis.readInt();
                if (response >= 10) {
                    if(clientSocket != null) {
                        System.out.println("Got socket in login!!!! Closed?: " + clientSocket.isClosed());
                    } else {
                        System.out.println("blegh");
                    }
                    new ActiveClient().start();
                    //sendMessageToServer("Hej hej!", null); //TODO remove
                } else {
                    System.out.println("Client: Code Mismatch");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Client: Login Error");
            }
            if(clientSocket != null) {
                System.out.println("Got socket at the end of login!!!! Closed?: " + clientSocket.isClosed());
            } else {
                System.out.println("blegh");
            }
        }
    }

    private class ClientMessage extends Thread {
        private Message message;

        private ClientMessage(Message message) {
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println("Client Message Run");
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
                oos.writeObject(message);
                oos.flush();
            } catch (IOException ioe) {
                System.out.println("Client: Message Error: " + ioe.getMessage());
                ioe.printStackTrace();
            }
        }
    }
}
