package adrenaline.client;

import adrenaline.Color;

import java.util.ArrayList;

/**
 *
 *
 * The ConnectionHandler interface,The RMIHandler and SocketHandler will implements it.
 * send message from client to server,and received the return value.
 *
 */
public interface ConnectionHandler {

    /**
     *
     * To unregister for client
     *
     */
    void unregister();

    /**
     *
     * To set nickname of player from client terminal to server terminal
     *
     * @param nickname The nickname string
     */
    void setNickname(String nickname);


    /**
     *
     * To set the lobby from client terminal to server terminal
     *
     * @param LobbyID The lobbyID string
     */
    void setMyLobby(String LobbyID);

    /**
     *
     * To select avatar operation for player
     * at AvatarSelectionState from client terminal to server terminal
     *
     * @param color The color of avatar witch the player selected
     */
    void selectAvatar(Color color);

    /**
     *
     * To select powerup card for player
     * from client terminal to server terminal
     *
     * @param powerupID The powerupId witch the player selected
     */
    void selectPowerUp(int powerupID);

    /**
     *
     * To select weapon for player from client terminal to server terminal
     *
     * @param weaponID The  weaponID witch the player selected
     */
    void selectWeapon(int weaponID);

    /**
     *
     * To select ammo for player from client terminal to server terminal
     *
     * @param color The ammo color witch the player selected
     */
    void selectAmmo(Color color);

    /**
     *
     * To send Map(from 1 to 4) and skull(from 5 to 8) set from client terminal to server terminal
     *
     * @param selectedMap The map number from 1 to 4
     * @param selectedSkull The number of Skull from 5 to 8
     *
     */
    void sendSettings(int selectedMap, int selectedSkull);

    /**
     *
     * To send a chat message from client terminal to server terminal
     *
     * @param message The message string witch wait for be send
     *
     */
    void sendChatMessage(String message);

    /**
     *
     * To send the run action for player from client terminal to server terminal
     *
     */
    void run();

    /**
     *
     * To send the grab action for player from client terminal to server terminal
     */
    void grab();

    /**
     *
     * To send the shoot action for player from client terminal to server terminal
     */
    void shoot();

    /**
     *
     * To send the go back action for player from client terminal to server terminal
     */
    void back();

    /**
     *
     * To send the end turn action for player from client terminal to server terminal
     *
     */
    void endTurn();

    /**
     *
     *
     * To send the square selection from client terminal to server terminal
     *
     * @param index The square index from 0 to 11
     *
     */
    void selectSquare(int index);

    /**
     *
     * To send the firemode selection from client terminal to server terminal
     *
     * @param firemode The firemode index from 0 to 2
     *
     */
    void selectFiremode(int firemode);

    /**
     *
     *
     * To send the player selection from client terminal to server terminal
     *
     * @param targets The ArrayList witch contain players' color
     */
    void selectPlayers(ArrayList<Color> targets);


    /**
     *
     * To send especial move action at MoveEnemyState or MoveSelfState from client terminal to server terminal
     *
     *
     */
    void moveSubAction();

    /**
     *
     * To send selection final frenzy action from client terminal to server terminal
     * <p>
     *     The action index range depending on the mode
     *     In mode 0, action index from 0 to 2
     *     In mode 1, action index from 0 to 1
     * </p>
     *
     *
     * @param action The action index range depending on the mode from 0 to 1(or 2)
     *
     */
    void selectFinalFrenzyAction(int action);

    /**
     *
     * The getter for ClientID
     *
     * @return The clientID string
     */
    String getClientID();

    /**
     *
     * The getter for LobbyID
     *
     * @return The lobbyID string
     */
    String getMyLobbyID();

    /**
     *
     * For close the connection from server to client
     *
     *
     */
    void closeConnection();
}
