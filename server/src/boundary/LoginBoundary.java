package boundary;

import control.LoginHandler;
import shared_entity.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginBoundary {
    private LoginHandler.ClientLogin clientLogin;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginBoundary(LoginHandler.ClientLogin clientLogin, ObjectOutputStream oos, ObjectInputStream ois) {
        this.clientLogin = clientLogin;
        this.oos = oos;
        this.ois = ois;
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

    public void writeUserMessageToClient(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Login Boundary: Write IO Exception");
        }
    }


    public String readUsernameFromClient() {
        String username = "";
        try {
            System.out.println("Awaiting String read");
            username = ois.readUTF();
            System.out.println("Read String: " + username);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Login Boundary: Read IO Exception");
        }
        return username;
    }

    public byte[] readProfilePictureFromClient() {
        byte[] profilePicture = null;
        try {
            profilePicture = (byte[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Server Login Boundary: Read IO Exception");
        }
        return profilePicture;
    }
}
