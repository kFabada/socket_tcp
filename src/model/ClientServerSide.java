package model;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class ClientServerSide {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Server server;

    public ClientServerSide(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
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


    public void sendMessage(String message) {
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void redirectMessage(String message) {
        server.getSocketClientList().forEach(client -> {
            if (client != this) {
                client.sendMessage(message);
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
                redirectMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
