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
        message = replaceEmojis(message);
        notifyObservers(new ChatUpdateMessage(nickname, senderColor, message));
    }

    public void addServerMessage(String message){
        notifyObservers(new ChatUpdateMessage("SERVER", Color.WHITE, message));
    }

    private String replaceEmojis(String message){
        String newMessage;
        newMessage = message;
        newMessage = newMessage.replace("<3", "❤");
        newMessage = newMessage.replace("qq", "qq \uD83D\uDCA6");
        newMessage = newMessage.replace("/gun", "\uD83D\uDD2B");
        newMessage = newMessage.replace("BOOM", "BOOM \uD83D\uDCA5");
        return newMessage;
    }
}
