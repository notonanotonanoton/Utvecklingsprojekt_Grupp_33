package boundary;

import control.LoginHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The LoginBoundary class handles communication between the server and a client during the login process.
 * It provides methods to read and write login-related information to and from the client.
 */
public class LoginBoundary {
    private LoginHandler.ClientLogin clientLogin;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Constructor for the LoginBoundary class.
     * @param clientLogin The ClientLogin object associated with this boundary.
     * @param oos The ObjectOutputStream for writing objects to the client.
     * @param ois The ObjectInputStream for reading objects from the client.
     */
    public LoginBoundary(LoginHandler.ClientLogin clientLogin, ObjectOutputStream oos, ObjectInputStream ois) {
        this.clientLogin = clientLogin;
        this.oos = oos;
        this.ois = ois;
    }

    /**
     * Method to write a response code to the client.
     * @param responseNbr The response code to be written.
     */
    public void writeResponseToClient(int responseNbr) {
        try {
            oos.writeInt(responseNbr);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Login Boundary: Write IO Exception");
        }
    }

    /**
     * Method to write a user message to the client.
     * @param messageObject The message object to be written.
     */
    public void writeUserMessageToClient(Object messageObject) {
        try {
            oos.writeObject(messageObject);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Server Login Boundary: Write IO Exception");
        }
    }

    /**
     * Method to read the username from the client.
     * @return The username read from the client.
     */
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

    /**
     * Method to read the profile picture from the client.
     * @return The profile picture data read from the client.
     */
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
