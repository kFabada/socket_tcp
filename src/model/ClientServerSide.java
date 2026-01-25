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
    private CommandParse commandParse;

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
            closeConnect();
        }
    }

    public void redirectMessage(String message) throws RuntimeException{
        if (this.username.isEmpty()){
            throw new RuntimeException();
        }


        server.getSocketClientList().forEach(client -> {
            if (client != this) {
                client.sendMessage("from: " + username + " message: " + message.replaceFirst(":", ""));
            }
        });
    }

    public void receiveMessage() {
        boolean run = true;
        while (run) {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                String message = inputStream.readUTF();
                this.command(message);
            } catch (IOException e) {
                closeConnect();
                run = false;
            }
        }
    }

    public void closeConnect(){
        try{
            if(inputStream != null) inputStream.close();
            if(outputStream != null)  outputStream.close();

            server.getSocketClientList().remove(this);

            System.out.println("close socket from " + username + " ip: " + socket.getLocalAddress().getHostAddress() + " port: " + socket.getPort());

            if(username != null){
                server.getListUsername().remove(username);
            }

            if(socket != null) socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void command(String message){

        if(commandParse == null){
            commandParse = new CommandParse(this);
        }

        ServerWarningMessage status = commandParse.parse(message);

        if(status != null){
            if(server.getPoolWarningMessage() == null){
                server.setPoolWarningMessage(Executors.newCachedThreadPool());
            }

            ServerThreadWarningMessage threadWarningMessage = new ServerThreadWarningMessage(server, this, status);
            server.getPoolWarningMessage().execute(threadWarningMessage);
       }
    }

    public String listUsers(){
      return server.getListUsername().stream().reduce("", (accumulator, element) -> {
            if(element.equals(username)) return "";
            return accumulator + "\n" + element;
        });
    }
}
