package control;

import boundary.ClientMainView;
import shared_entity.message.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private ClientMainView mainView;

    public Client(Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.clientSocket = clientSocket;
        mainView = new ClientMainView(this, oos, ois);
    }
}
