package adrenaline.server.controller;

/**
 *
 * The turn timer class implements Runnable for turn timer
 *
 */
public class TurnTimer  implements Runnable {

    private Lobby callBackLobby;

    /**
     *
     * The constructor of turn timer
     *
     * @param lobby Pass the lobby reference to callBackLobby
     */
    TurnTimer(Lobby lobby){
        callBackLobby = lobby;
    }


    /**
     *
     * To fo endTrun action when the timer finished
     *
     */
    @Override
    public void run() { callBackLobby.endTurn(true); }
}
