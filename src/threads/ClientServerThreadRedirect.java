package threads;

import model.ClientServerSide;

public class ClientServerThreadRedirect implements Runnable {
    private ClientServerSide clientServerSide;

    public ClientServerThreadRedirect(ClientServerSide clientServerSide) {
        this.clientServerSide = clientServerSide;
    }

    @Override
    public void run() {
        System.out.println("new user connect");
        clientServerSide.receiveMessage();
    }
}
