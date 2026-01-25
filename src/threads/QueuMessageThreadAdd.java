package threads;

import model.ClientServerSideQueu;
import model.QueuMessage;

public final class QueuMessageThreadAdd implements Runnable{
    private final QueuMessage queuMessage;
    private final ClientServerSideQueu client;

    public QueuMessageThreadAdd(QueuMessage queuMessage, ClientServerSideQueu client) {
        this.queuMessage = queuMessage;
        this.client = client;
    }

    @Override
    public void run() {
        queuMessage.add(client);
    }
}
