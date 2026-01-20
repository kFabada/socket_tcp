package enums;

public enum ServerWarningMessage {
    REGISTER_USERNAME ("REGISTER USERNAME WITH /:username-"),
    INVALID_MESSAGE("INVALID COMMAND"),
    USERNAME_ACCEPT("REGISTER USERNAME ACCEPT"),
    MESSAGE_BLOCK("USERNAME DON'T REGISTER");

    private final String message;

    ServerWarningMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
