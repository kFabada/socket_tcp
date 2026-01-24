package enums.factory;

import enums.ServerWarningMessage;
import model.Server;
import threads.ServerThreadWarningMessage;

public class ServerWaningMessageFactory {
    private final String message;

    public ServerWaningMessageFactory(String message) {
        this.message = message;
    }

    public ServerWarningMessage warningMessageFactory(){
        return ServerWarningMessage.USER_LIST.setMessage(message);
    }
}
