package model;

import enums.ServerState;
import enums.ServerWarningMessage;
import threads.ClientServerThreadRedirect;
import threads.QueuMessageThreadTake;
import threads.ServerThreadRegisterClient;
import threads.ServerThreadWarningMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientServerSide> socketClientList = new ArrayList<>();
    private Set<String> listUsername = new HashSet<>();
    private final int port;
    private ExecutorService poolWarningMessage;
    private ExecutorService poolGenericContex;
    private ServerState serverState = ServerState.CLOSE;
    private QueuMessage queuMessage = new QueuMessage();

    public Server(int port) {
        this.port = port;
        this.poolGenericContex = Executors.newFixedThreadPool(5);
        this.poolWarningMessage = Executors.newFixedThreadPool(3);
        setLimit();
        startQueu();
    }

    private void startQueu(){
       Thread th = new Thread(new QueuMessageThreadTake(queuMessage));
       th.start();
    }

    private void setLimit(){
        try {
            poolGenericContex.awaitTermination(100, TimeUnit.MILLISECONDS);
            poolWarningMessage.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ExecutorService getPoolWarningMessage() {
        return poolWarningMessage;
    }

    public void setPoolWarningMessage(ExecutorService poolWarningMessage) {
        this.poolWarningMessage = poolWarningMessage;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public Set<String> getListUsername() {
        return listUsername;
    }

    public void setListUsername(Set<String> listUsername) {
        this.listUsername = listUsername;
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
        while (true) {
            try {
                Socket client = serverSocket.accept();
                poolGenericContex.execute(new ServerThreadRegisterClient(this, client));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registerClient(Socket client) {
        try {
            client.setTcpNoDelay(true);
            ClientServerSide clientServerSide = new ClientServerSide(client, this, queuMessage);
            socketClientList.add(clientServerSide);
            ServerThreadWarningMessage warningMessage = new ServerThreadWarningMessage(this, clientServerSide, ServerWarningMessage.REGISTER_USERNAME);

            poolWarningMessage.execute(warningMessage);

            ClientServerThreadRedirect clientServerThreadRedirect = new ClientServerThreadRedirect(clientServerSide);
            Thread threadClientServerRedirect = new Thread(clientServerThreadRedirect);
            threadClientServerRedirect.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void warningMessage(ServerWarningMessage message, ClientServerSide clientServerSide) {
        DataOutputStream out;
        Socket client = clientServerSide.getSocket();

        try {
            out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(message.getMessage());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
