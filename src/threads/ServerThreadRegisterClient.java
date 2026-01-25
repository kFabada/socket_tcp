package threads;

import model.Server;

import java.net.Socket;

public class ServerThreadRegisterClient implements Runnable{
    private final Server server;
    private final Socket socket;

    public ServerThreadRegisterClient(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        server.registerClient(socket);
    }
}
