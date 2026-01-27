package threads;

import model.ClientManagerMessageState;

public final class ClientThreadStateMessage implements Runnable{
    private final ClientManagerMessageState stateMessage;

    public ClientThreadStateMessage(ClientManagerMessageState stateMessage) {
        this.stateMessage = stateMessage;
    }

    @Override
    public void run() {
        stateMessage.read();
    }
}
