package adrenaline;

import adrenaline.client.controller.GameController;

public class ChatUpdateMessage implements UpdateMessage {
    private String nickname;
    private Color senderColor;
    private String message;


    public ChatUpdateMessage(String nickname, Color senderColor, String message){
        this.nickname = nickname;
        this.senderColor = senderColor;
        this.message = message;
    }

    public void applyUpdate(GameController clientGameController) {
        clientGameController.updateChat(nickname, senderColor, message);
    }
}
