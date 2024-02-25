import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import control.Client;
import control.Server;


public class ClientTest {

    @Test
    public void testClientServerCommunication() {

        Server server = new Server(3343, 3344);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Client client = new Client("127.0.0.1", 3343, 3344, "GroundZeroGreta");
        client.sendMessageToServer("Hej hej!", null);
    }
}