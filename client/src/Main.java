import control.Client;
import control.LoginClient;

public class Main {
    public static void main(String[] args) {
        LoginClient client = new LoginClient("127.0.0.1",3343, "GroundZeroGreta");
        //new Client("127.0.0.1",3343, "BarnacleBarnaby");
    }
}