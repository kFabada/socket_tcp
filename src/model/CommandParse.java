package model;

import enums.ServerWarningMessage;

public class CommandParse {
    private final ClientServerSide clientServerSide;

    public CommandParse(ClientServerSide clientServerSide) {
        this.clientServerSide = clientServerSide;
    }

    public ServerWarningMessage parse(String message){
        ServerWarningMessage serverWarningMessage = ServerWarningMessage.INVALID_MESSAGE;

        if(message.startsWith(":")){
            try {
                clientServerSide.redirectMessage(message);
                return null;
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
                stringBuilder = new StringBuilder();

                while (i <= (data.length - 1)){
                    stringBuilder.append(data[i]);
                    i++;
                }

                if(!clientServerSide.getServer().getListUsername().contains(stringBuilder.toString())){
                    clientServerSide.setUsername(stringBuilder.toString());
                    clientServerSide.getServer().getListUsername().add(stringBuilder.toString());
                    clientServerSide.setRegisterUsername(true);
                    serverWarningMessage = ServerWarningMessage.USERNAME_ACCEPT;
                }else {
                    serverWarningMessage = ServerWarningMessage.USERNAME_ALREADY_EXISTS;
                }
            }
        }
        return serverWarningMessage;
    }
}
