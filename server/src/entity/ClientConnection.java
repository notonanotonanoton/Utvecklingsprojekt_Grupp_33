package entity;

import control.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Server.ClientHandler clientHandler;

    public ClientConnection(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOutputStream() {
        if (ois == null) {
            System.out.println("Warning! Get called on null OutputStream");
        }
        return oos;
    }

    public void setOutputStream(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getInputStream() {
        if (ois == null) {
            System.out.println("Warning! Get called on null InputStream");
        }
        return ois;
    }

    public void setInputStream(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void addThread(Server.ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }
    public Server.ClientHandler getThread() {
        return clientHandler;
    }
}
