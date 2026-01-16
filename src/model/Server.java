package model;

import enums.ServerState;
import threads.ClientServerThreadRedirect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientServerSide> socketClientList = new ArrayList<>();
    private final int port;
    private ServerState serverState = ServerState.CLOSE;

    public Server(int port) {
        this.port = port;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public int getPort() {
        return port;
    }

    public List<ClientServerSide> getSocketClientList() {
        return socketClientList;
    }

    public void setSocketClientList(List<ClientServerSide> socketClientList) {
        this.socketClientList = socketClientList;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        try {
            serverSocket = new ServerSocket(port);
            serverState = ServerState.RUN;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listenClients() {
        while (true){
            try {
                Socket client = serverSocket.accept();
                client.setTcpNoDelay(true);
                ClientServerSide clientServerSide = new ClientServerSide(client, this);

                socketClientList.add(clientServerSide);

                ClientServerThreadRedirect clientServerThreadRedirect = new ClientServerThreadRedirect(clientServerSide);
                Thread threadClientServerRedirect = new Thread(clientServerThreadRedirect);
                threadClientServerRedirect.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
