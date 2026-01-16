package threads;

import model.Client;

public class ClientThreadReceive implements Runnable{
    private Client client;

    public ClientThreadReceive(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.receiveMessage();
    }
}
