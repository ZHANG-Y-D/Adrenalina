package adrenaline.server.model;

import adrenaline.ChatUpdateMessage;
import adrenaline.Color;
import adrenaline.server.Observable;
import adrenaline.server.network.Client;

import java.util.ArrayList;

public class Chat extends Observable {

    public Chat(ArrayList<Client> clients){
        clients.forEach(this::attach);
    }

    public void addMessage(String nickname, Color senderColor, String message){
        notifyObservers(new ChatUpdateMessage(nickname, senderColor, message));
    }
}
