package threads;

import model.Client;

public class ClientThreadSend implements Runnable{
    private Client client;

    public ClientThreadSend(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.sendMessage();
    }
}
