import model.Server;
import threads.ServerThreadListen;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(8080);
        ServerThreadListen serverThreadListen = new ServerThreadListen(server);

        Thread threadServer = new Thread(serverThreadListen);
        threadServer.start();
    }
}
