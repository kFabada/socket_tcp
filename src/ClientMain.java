import model.Client;
import threads.ClientThreadReceive;
import threads.ClientThreadSend;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 8080);
        ClientThreadSend clientThreadSend = new ClientThreadSend(client);
        ClientThreadReceive clientThreadReceive = new ClientThreadReceive(client);
        client.connectServer();

        Thread threadSend = new Thread(clientThreadSend);
        Thread threadReceive = new Thread(clientThreadReceive);

        threadSend.start();
        threadReceive.start();
    }
}
