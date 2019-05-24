package adrenaline.server.controller;

import adrenaline.server.model.Player;

import java.util.ArrayList;

 public class Move {



    /**
     * To do the move in turn, move to valid square
     *
     * @param player The player who has to move
     * @param movePosition which position the player has to move
     * @param maxStepsCanMove Max steps this player can move
     *
     * @return  true: move successful; false move error, cause of invalid square
     */

    public static boolean moveInturn(Player player,int movePosition,int maxStepsCanMove){

        ArrayList<Integer> validSquare;
        int playerActualPosition;

        playerActualPosition = player.getPosition();
        validSquare = player.getLobby().getMap().getValidSquares(playerActualPosition,maxStepsCanMove);

        if (validSquare.contains(movePosition)) {
            player.setPosition(movePosition);
            return true;
        }
        else
            return false;
    }


}
