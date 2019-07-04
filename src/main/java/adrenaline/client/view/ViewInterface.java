package adrenaline.client.view;


import adrenaline.Color;
import adrenaline.client.controller.GameController;

import java.util.ArrayList;

/**
 *
 *
 * The view interface, All view will implement it,and set behave for every stage
 *
 */
public interface ViewInterface {

    /**
     *
     * For show the error message from server
     *
     * @param error
     */
    void showError(String error);

    /**
     *
     * For show the OK message from server
     *
     * @param message
     */
    void showMessage(String message);

    /**
     *
     * For get the change stage signal from game controller
     *
     */
    void changeStage();

    /**
     *
     *
     * For the current game controller for new stage
     *
     * @param gameController The gameController reference
     */
    void setGameController(GameController gameController);

    /**
     *
     * Notify the server timer is stared
     *
     * @param duration The timer duration
     * @param comment The comment, it will remind now it's witch player's turn
     */
    void notifyTimer(Integer duration, String comment);

    /**
     *
     * Remind view to update chat message
     *
     * @param nickname The nickname of the player who send this chat
     * @param senderColor The color of the player who send this chat
     * @param message The chat message string
     */
    void newChatMessage(String nickname, Color senderColor, String message);

    /**
     *
     * Remind the view to show valid Squares
     *
     * @param validSquares A reference of an ArrayList<Integer> witch contain all valid square
     */
    void showValidSquares(ArrayList<Integer> validSquares);
}
