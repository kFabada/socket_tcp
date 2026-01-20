package enums;

public enum ServerWarningMessage {
    REGISTER_USERNAME ("SERVER MESSAGE: REGISTER USERNAME WITH /:username-"),
    INVALID_MESSAGE("SERVER MESSAGE: INVALID COMMAND"),
    USERNAME_ACCEPT("SERVER MESSAGE: REGISTER USERNAME ACCEPT"),
    USERNAME_ALREADY_EXISTS("SERVER MESSAGE: USERNAME ALREADY EXISTS"),
    MESSAGE_BLOCK("SERVER MESSAGE: USERNAME DON'T REGISTER");

    private final String message;

    ServerWarningMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
