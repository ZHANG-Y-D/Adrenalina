package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

public class SameRoomConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(ArrayList<Player> targets, Map map) {
        ArrayList<Integer> room = map.getRoomSquares(targets.get(0).getPosition());
        for(Player trg : targets){
            if(!(room.equals(map.getRoomSquares(trg.getPosition())))) return false;
        }
        return true;
    }
}
