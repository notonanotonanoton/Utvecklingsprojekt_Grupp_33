package boundary;

import control.LoginClient;
import shared_entity.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginView {
    private LoginClient loginClient;
    private LoginFrame loginFrame;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginView(LoginClient loginClient, ObjectOutputStream oos, ObjectInputStream ois) {
        this.loginClient = loginClient;
        this.oos = oos;
        this.ois = ois;
        this.loginFrame = new LoginFrame(this);
    }

    //TODO add real implementation

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

    public Message getUserMessageFromServer() throws IOException, ClassNotFoundException {
        return (Message)ois.readObject();
    }

    public void closeLoginWindow() {
        loginFrame.dispose();
    }
}
