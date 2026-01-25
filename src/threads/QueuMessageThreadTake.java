package threads;

import model.QueuMessage;

public final class QueuMessageThreadTake implements Runnable{
    private final QueuMessage queuMessage;

    public QueuMessageThreadTake(QueuMessage queuMessage) {
        this.queuMessage = queuMessage;
    }

    @Override
    public void run() {
        queuMessage.takeThreadLoop();
    }
}
