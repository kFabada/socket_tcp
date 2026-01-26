package model;

import java.util.concurrent.LinkedBlockingQueue;

public class QueuMessage {
    private volatile LinkedBlockingQueue<ClientServerSideQueu> queue = new LinkedBlockingQueue<>();
    private volatile boolean read = false;

    public synchronized void add(ClientServerSideQueu client){
        queue.add(client);
        read = true;
        notify();
    }

    public synchronized void takeThreadLoop(){
        while (true){
            if(!queue.isEmpty()){
                try {
                    ClientServerSideQueu client = queue.take();
                    client.getClientServerSide().sendMessage(client.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                read = false;
            }

            while (!read){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
