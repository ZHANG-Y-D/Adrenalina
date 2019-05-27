package adrenaline.client.view;

import adrenaline.server.controller.states.GameState;

import java.util.List;

public abstract class View {


    public abstract void showMessage(String message);

    public abstract void reportError(String error);

    public abstract void chooseState(List<GameState> gameStateList);

    public void showState(GameState gameState){

    }

}
