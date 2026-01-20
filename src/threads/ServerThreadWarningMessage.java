package threads;

import enums.ServerWarningMessage;
import model.ClientServerSide;
import model.Server;

public class ServerThreadWarningMessage implements Runnable{
    private Server server;
    private ServerWarningMessage message;
    private ClientServerSide clientServerSide;

    public ServerThreadWarningMessage(Server server, ClientServerSide clientServerSide, ServerWarningMessage message) {
        this.server = server;
        this.clientServerSide = clientServerSide;
        this.message = message;
    }

    @Override
    public void run() {
        server.warningMessage(this.message, this.clientServerSide);
    }
}
