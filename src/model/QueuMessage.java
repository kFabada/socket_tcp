package model;

import java.util.concurrent.LinkedBlockingQueue;

public class QueuMessage {
    private volatile LinkedBlockingQueue<ClientServerSideQueu> queue = new LinkedBlockingQueue<>();

    public void add(ClientServerSideQueu client){
        queue.add(client);
    }

    public void takeThreadLoop(){
        while (true){
            if(!queue.isEmpty()){
                try {
                    ClientServerSideQueu client = queue.take();
                    client.getClientServerSide().sendMessage(client.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
