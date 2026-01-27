package model;

import threads.ClientThreadGetMessage;
import threads.ClientThreadStateMessage;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Scanner scanner;
    private ClientManagerMessageState messageState;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.scanner = new Scanner(System.in);
        this.messageState = new ClientManagerMessageState();

        new Thread(new ClientThreadStateMessage(messageState)).start();
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

                ClientThreadGetMessage c = new ClientThreadGetMessage(messageState);
                new Thread(c).start();
                String message = c.getMessage();

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
            }
        }

    }
}
