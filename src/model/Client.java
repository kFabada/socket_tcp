package model;

import threads.ClientThreadGetMessage;
import threads.ClientThreadStateMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ExecutorService pool;
    private ClientManagerMessageState messageState;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.messageState = new ClientManagerMessageState();
        this.pool = Executors.newFixedThreadPool(3);
        this.pool.execute(new ClientThreadStateMessage(messageState));
    }

    public void connectServer() {
        try {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage() {
        while (true){
            try {
                outputStream = new DataOutputStream(socket.getOutputStream());

                ClientThreadGetMessage state = new ClientThreadGetMessage(messageState);
                pool.execute(state);
                String message = state.getMessage();

                if(!message.isBlank()){
                    outputStream.writeUTF(message);
                    outputStream.flush();

                    if(message.startsWith("/:close")){
                        closeConnection();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeConnection(){
        try{
            if(inputStream != null) inputStream.close();
            if(outputStream != null)  outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            Thread.interrupted();
        }
    }

    public void receiveMessage() {
        while (true){
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                String i = inputStream.readUTF();
                System.out.println(i);
            } catch (IOException e) {
               Thread.interrupted();
               break;
            }
        }

    }
}
