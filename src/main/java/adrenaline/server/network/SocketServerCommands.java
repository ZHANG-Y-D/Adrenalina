package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.GameServer;
import adrenaline.server.ServerAPI;


public class SocketServerCommands implements ServerAPI {

    private final GameServer mainServer;

    public SocketServerCommands(GameServer mainServer){
        this.mainServer = mainServer;
    }

    public String registerRMIClient(ClientAPI clientAPI){
        return "KO (Invalid operation for socket clients)";
    }

    public String setNickname(String clientID, String nickname) {
        if(nickname.length()<1) return "Nickname must contain at least 1 character!";
        if(mainServer.setNickname(clientID, nickname)) return "/OK";
        else return "This nickname is already taken!";
    }

    public String unregisterClient(String clientID){
        this.mainServer.unregisterClient(clientID);
        System.out.println("Client closed his Socket session");
        return "OK";
    }


}
