package model;

import enums.ServerWarningMessage;
import threads.ServerThreadWarningMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ClientServerSide {
    private Socket socket;
    private String username;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Server server;
    private boolean registerUsername = false;

    public ClientServerSide(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public ClientServerSide(Socket socket, Server server, String username) {
        this.socket = socket;
        this.server = server;
        this.username = username;
    }

    public boolean getRegisterUsername() {
        return registerUsername;
    }

    public void setRegisterUsername(boolean registerUsername) {
        this.registerUsername = registerUsername;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void sendMessage(String message) {
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void redirectMessage(String message) throws RuntimeException{
        if (this.username.isEmpty()){
            throw new RuntimeException();
        }


        server.getSocketClientList().forEach(client -> {
            if (client != this) {
                client.sendMessage("from: " + username + " -> " + message);
            }
        });
    }

    public String messageFormat(byte[] message) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i <= message.length - 1; i++) {
            builder.append(message[i]);
        }
        return builder.toString();
    }

    public void receiveMessage() {
        while (true) {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                String message = inputStream.readUTF();
                this.command(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void command(String message){
        CommandParse commandParse = new CommandParse(message, this);
        ServerWarningMessage status = commandParse.parse();

        if(status != null){
            if(server.getPoolWarningMessage() == null){
                server.setPoolWarningMessage(Executors.newCachedThreadPool());
            }

            ServerThreadWarningMessage threadWarningMessage = new ServerThreadWarningMessage(server, this, status);
            server.getPoolWarningMessage().execute(threadWarningMessage);
       }
    }
}
