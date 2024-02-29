package boundary;

import control.LoginHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginBoundary extends Thread {
    LoginHandler.ClientLogin clientLogin;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public LoginBoundary(LoginHandler.ClientLogin clientLogin, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        this.clientLogin = clientLogin;
        this.oos = oos;
        this.ois = ois;
        start();
    }

    public void writeResponseToClient(int responseNbr) {
        try {
            oos.writeInt(responseNbr);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Login Boundary: Write IO Exception");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Awaiting String read");
            String username = ois.readUTF();
            System.out.println("Read String: " + username);
            clientLogin.loginUser(username);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Login Boundary: Read IO Exception");
        }
    }
}
