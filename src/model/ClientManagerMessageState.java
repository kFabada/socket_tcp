package model;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientManagerMessageState {
    private LinkedBlockingQueue<String> message = new LinkedBlockingQueue<>();
    private final Scanner sc = new Scanner(System.in);
    private volatile boolean read = false;

    public synchronized String getMessage(){
        if(message.isEmpty()){
            while (!read){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return take();
    }

    private String take(){
        String temp;
        try {
            temp = message.take();
            if(message.isEmpty()) read = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }

    public void read(){
        while (true){
           String in = sc.nextLine();

           synchronized (this){
               if(!in.isEmpty()) {
                   message.add(in);
                   read = true;
                   notify();
                   System.out.println("notifico o get");
               }
           }

           if(in.startsWith("/:close")){
               break;
           }
        }
    }
}
