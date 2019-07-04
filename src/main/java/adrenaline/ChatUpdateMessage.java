package adrenaline;

import adrenaline.client.controller.GameController;


/**
 *
 * To update chat massage.The realize of observer pattern
 *
 */
public class ChatUpdateMessage implements UpdateMessage {
    private String nickname;
    private Color senderColor;
    private String message;


    /**
     *
     *
     * For update the message
     * @param nickname The nickname string
     * @param senderColor The color of sender
     * @param message The message
     */
    public ChatUpdateMessage(String nickname, Color senderColor, String message){
        this.nickname = nickname;
        this.senderColor = senderColor;
        this.message = message;
    }

    /**
     *
     * For update the message
     * @param clientGameController The game controller id client
     */
    public void applyUpdate(GameController clientGameController) {
        clientGameController.updateChat(nickname, senderColor, message);
    }
}
