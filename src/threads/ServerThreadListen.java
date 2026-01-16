package threads;

import enums.ServerState;
import model.Server;

public class ServerThreadListen implements Runnable {
    private Server server;

    public ServerThreadListen(Server server){
        this.server = server;
    }

    @Override
    public void run() {
         if(server.getServerState() == ServerState.CLOSE){
             server.runServer();
         }
         server.listenClients();
    }
}
