package adrenaline.server.model;

import adrenaline.ChatUpdateMessage;
import adrenaline.Color;
import adrenaline.server.Observable;
import adrenaline.server.network.Client;

import java.util.ArrayList;

/**
 *
 * The chat class for build chat message
 *
 */
public class Chat extends Observable {


    /**
     *
     * The constructor of the chat, attach every client to Observer
     *
     * @param clients The client ArrayList
     */
    public Chat(ArrayList<Client> clients){
        clients.forEach(this::attach);
    }

    /**
     *
     * Add a new player's chat message
     *
     * @param nickname The nickname of the player who send this chat
     * @param senderColor The color of the player who send this chat
     * @param message The player's chat message string
     */
    public void addMessage(String nickname, Color senderColor, String message){
        message = replaceEmojis(message);
        notifyObservers(new ChatUpdateMessage(nickname, senderColor, message));
    }

    /**
     *
     * Add a new server's chat message
     *
     * @param message The server chat message string
     */
    public void addServerMessage(String message){
        notifyObservers(new ChatUpdateMessage("SERVER", Color.WHITE, message));
    }


    /**
     *
     * Replace a field to Emoji
     *
     * @param message The chat message string
     * @return The replaced chat message
     */
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
