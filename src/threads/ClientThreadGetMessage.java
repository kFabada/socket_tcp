package threads;

import model.ClientManagerMessageState;

public class ClientThreadGetMessage implements Runnable{
    private final ClientManagerMessageState clientManagerMessageState;
    private String message = "";
    private boolean complete = false;

    public ClientThreadGetMessage(ClientManagerMessageState clientManagerMessageState) {
        this.clientManagerMessageState = clientManagerMessageState;
    }

    public synchronized String getMessage(){
        while (!complete){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return message;
    }


    @Override
    public void run() {
        synchronized (this){
            message = clientManagerMessageState.getMessage();
            complete = true;
            notifyAll();
        }
    }
}
