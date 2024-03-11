package boundary;

import control.LoginHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginBoundary {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginBoundary(ObjectOutputStream oos, ObjectInputStream ois) {
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

    public void writeUserMessageToClient(Object messageObject) {
        try {
            oos.writeObject(messageObject);
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
