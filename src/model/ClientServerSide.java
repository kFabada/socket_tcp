package model;

import enums.ServerWarningMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientServerSide {
    private Socket socket;
    private String username;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Server server;

    public ClientServerSide(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public ClientServerSide(Socket socket, Server server, String username) {
        this.socket = socket;
        this.server = server;
        this.username = username;
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

    private void comand(String message){
        boolean erro = false;

        if(message.startsWith("/:") && message.contains("-")){
            String metterComand = "username";
            char prefix = '-';
            char[] data = message.toCharArray();

            StringBuilder stringBuilder = new StringBuilder();

            char c;
            int i = 2;
            while ((c = data[i]) != prefix){
                stringBuilder.append(c);
                i++;
            }

            if(stringBuilder.toString().equals(metterComand)){
                i++;
                while (i <= (data.length - 1)){
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append(data[i]);
                    i++;
                }
                this.username = stringBuilder.toString();
            }else{
                erro = true;
            }
        }else{
            erro = true;
        }

        if(erro){
            this.server.warningMessage(ServerWarningMessage.INVALID_MESSAGE, this);
        }else{
            this.server.warningMessage(ServerWarningMessage.USERNAME_ACCEPT, this);
        }
    }
}
