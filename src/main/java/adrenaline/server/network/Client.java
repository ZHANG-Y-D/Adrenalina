package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.util.ArrayList;


/**
 *
 * The clientAPI interface for some client set operation
 *
 */
public interface Client extends ClientAPI {

    /**
     *
     * The setter of ClientID
     * @param ID The ClientID string
     */
    void setClientID(String ID);

    /**
     *
     * The getter of ClientID
     *
     * @return The ClientID string
     */
    String getClientID();

    /**
     *
     * The getter of nick name
     *
     * @return The nickname string
     */
    String getNickname();


    /**
     *
     * Send the current player's nickname
     * from server terminal to client terminal
     *
     * @param nickname The nickname string
     * @return The set operation if is successful
     */
    boolean setNicknameInternal(String nickname);

    /**
     *
     * Setter of active status
     *
     * @param active True for still active
     */
    void setActive(boolean active);

    /**
     *
     * The getter of active status
     *
     * @return True for still active
     */
    boolean isActive();

    /**
     *
     * To send the lobbyID and players' nickname ArrayList from server terminal to client terminal
     *
     * @param lobby The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    void setLobby(Lobby lobby, ArrayList<String> nicknames);

    /**
     *
     * Send the player color set value from server terminal to client terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    void setPlayerColorInternal(String nickname, Color color);

    /**
     *
     * To kick a client when he did nothing during the whole turn
     * he have to do the reconnection to reconnect the server
     */
    void kickClient();
}
