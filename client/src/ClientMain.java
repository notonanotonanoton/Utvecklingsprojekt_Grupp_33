import control.LoginClient;

public class ClientMain {
    public static void main(String[] args) {
        new LoginClient("127.0.0.1", 3343);
        new LoginClient("127.0.0.1", 3343);
    }
}