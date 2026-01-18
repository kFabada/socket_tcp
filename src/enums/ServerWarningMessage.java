package enums;

public enum ServerWarningMessage {
    REGISTER_USERNAME ("REGISTER USERNAME WITH /:username-"),
    INVALID_MESSAGE("INVALID MESSAGE"),
    USERNAME_ACCEPT("REGISTER USERNAME ACCEPT");


    private final String message;

    ServerWarningMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
