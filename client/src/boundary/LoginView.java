package boundary;

import control.LoginClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The LoginView class is responsible for communication between the client-side application and the server during the login process.
 * It manages the elements related to logging in, including sending the username and profile picture to the server.
 */
public class LoginView {
    private LoginFrame loginFrame;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginView(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
        this.loginFrame = new LoginFrame(this);
    }

    public void sendUsernameToServer(String username) {
        try {
            System.out.println("writing username: " + username);
            oos.writeUTF(username);
            oos.flush();
        } catch (IOException ioe) {
            System.out.println("IOException - " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    public void sendProfilePictureToServer(byte[] profilePicture) {
        try {
            oos.writeObject(profilePicture);
            oos.flush();
        } catch (IOException ioe) {
            System.out.println("IOException - " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    public int readResponseFromServer() {
        int response = -1;
        try {
            response = ois.readInt();
        } catch (IOException ioe) {
            System.out.println("IOException - " + ioe.getMessage());
            ioe.printStackTrace();
        }
        return response;
    }

    public Object getUserMessageFromServer() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void closeLogin() {
        loginFrame.dispose();
    }

    public void selectProfilePicture() {
        loginFrame.selectProfilePicture();
    }
}
