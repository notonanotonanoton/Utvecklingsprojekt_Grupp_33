package boundary;

import control.LoginClient;
import shared_entity.message.Message;
import shared_entity.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginView {
    private LoginClient loginClient;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginView(LoginClient loginClient, ObjectOutputStream oos, ObjectInputStream ois) {
        this.loginClient = loginClient;
        this.oos = oos;
        this.ois = ois;
    }

    //TODO add real implementation
    public String enterUsername() {
        return loginClient.enterUsername();
    }

    public void sendUsernameToServer(String username) throws IOException {
        System.out.println("writing username: " + username);
        oos.writeUTF(username);
        oos.flush();
    }

    public int readResponseFromServer() throws IOException {
        return ois.readInt();
    }

    public Message getUserMessageFromServer() throws IOException, ClassNotFoundException {
        return (Message)ois.readObject();
    }
}
