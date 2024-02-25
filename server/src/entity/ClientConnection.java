package entity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    public ClientConnection(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this(socket);
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
}
