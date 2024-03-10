import control.LoginClient;

public class Client2 {
    public static void main(String[] args) {
        new LoginClient("127.0.0.1", 3343);
        new LoginClient("127.0.0.1", 3343);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new LoginClient("127.0.0.1", 3343);
    }
}