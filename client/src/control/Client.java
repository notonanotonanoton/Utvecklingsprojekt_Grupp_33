package control;

import shared_entity.message.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private String userName;
    private Socket clientSocket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public Client(Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.clientSocket = clientSocket;
        this.oos = oos;
        this.ois = ois;
        new ActiveClient().start();
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
            System.out.println("Client active");
            try {
                System.out.println("Client active, test sending message");
                sendMessageToServer("Hej hej!", null); //TODO remove test
                while (!clientSocket.isClosed()) {
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
}
