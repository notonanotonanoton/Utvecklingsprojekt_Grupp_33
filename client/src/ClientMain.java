import control.LoginClient;

public class ClientMain {
    public static void main(String[] args) {
        new LoginClient("127.0.0.1", 3343, "GroundZeroGreta");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        new LoginClient("127.0.0.1", 3343, "BarnacleBarnaby");
    }
}