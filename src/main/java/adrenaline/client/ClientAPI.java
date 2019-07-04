package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 *
 * The ClientAPI interface extends Remote,The Client interface will
 * implements it , and the Socket/Rmi wrapper/commands will implements Client interface
 *
 * It will send/received the command from server and client
 *
 */
public interface ClientAPI extends Remote {

    /**
     *
     * To send the current player's nickname from server terminal to client terminal
     *
     * @param nickname The nickname string
     *
     */
    void setNickname(String nickname) throws RemoteException;

    /**
     *
     * To send the lobbyID and players' nickname ArrayList from server terminal to client terminal
     *
     * @param lobbyID The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    void setLobby(String lobbyID, ArrayList<String> nicknames) throws RemoteException;


    /**
     *
     *
     * Send the player color set value from server terminal to client terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    void setPlayerColor(String nickname, Color color) throws  RemoteException;

    /**
     *
     *
     * To remind the client timer start.
     *
     * @param duration The duration of timer in seconds
     * @param comment The comment for this timer
     */
    void timerStarted(Integer duration, String comment) throws RemoteException;

    /**
     *
     * To send the valid squares from server terminal to client terminal
     *
     * @param validSquares The ArrayList of the valid squares
     *
     */
    void validSquaresInfo(ArrayList<Integer> validSquares) throws RemoteException;


    /**
     *
     *
     * To send the update message from server terminal to client terminal
     *
     * @param updatemsg The UpdateMessage reference
     *
     */
    void update(UpdateMessage updatemsg) throws RemoteException;

    /**
     *
     * To kick a client when he did nothing during the whole turn
     * he have to do the reconnection to reconnect the server
     */
    void kick() throws RemoteException;
}
