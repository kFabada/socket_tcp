package model;

import enums.ServerWarningMessage;

public class CommandParse {
    private String message;
    private ClientServerSide clientServerSide;

    public CommandParse(String message, ClientServerSide clientServerSide) {
        this.message = message;
        this.clientServerSide = clientServerSide;
    }

    public ServerWarningMessage parse(){
        ServerWarningMessage serverWarningMessage = ServerWarningMessage.INVALID_MESSAGE;

        if(message.startsWith(":")){
            try {
                clientServerSide.redirectMessage(message);
                serverWarningMessage = null;
            }catch (RuntimeException e) {
                serverWarningMessage = ServerWarningMessage.MESSAGE_BLOCK;
            }
        }

        if(message.startsWith("/:") && message.contains("-")){

            String metterCommand = "username";
            char prefix = '-';
            char[] data = message.toCharArray();

            StringBuilder stringBuilder = new StringBuilder();

            char c;
            int i = 2;
            while ((c = data[i]) != prefix){
                stringBuilder.append(c);
                i++;
            }

            if(stringBuilder.toString().equals(metterCommand)){
                i++;
                while (i <= (data.length - 1)){
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append(data[i]);
                    i++;
                }

                if(!clientServerSide.getServer().getListUsername().contains(stringBuilder.toString())){
                    clientServerSide.setUsername(stringBuilder.toString());
                    clientServerSide.getServer().getListUsername().add(stringBuilder.toString());
                    serverWarningMessage = ServerWarningMessage.USERNAME_ACCEPT;
                }else {
                    serverWarningMessage = ServerWarningMessage.USERNAME_ALREADY_EXISTS;
                }
            }
        }
        return serverWarningMessage;
    }
}
