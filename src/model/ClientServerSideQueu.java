package model;

public final class ClientServerSideQueu {
    private final ClientServerSide clientServerSide;
    private final String message;

    public ClientServerSideQueu(ClientServerSide clientServerSide, String message) {
        this.clientServerSide = clientServerSide;
        this.message = message;
    }

    public ClientServerSide getClientServerSide() {
        return clientServerSide;
    }

    public String getMessage() {
        return message;
    }
}
