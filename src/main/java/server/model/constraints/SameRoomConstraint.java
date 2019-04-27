package server.model.constraints;

import server.model.Map;
import server.model.PlayerCore;

import java.util.ArrayList;

public class SameRoomConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(PlayerCore shooter, ArrayList<PlayerCore> targets, Map map) {
        ArrayList<Integer> room = map.getRoomSquares(targets.get(0).getPosition());
        for(PlayerCore trg : targets){
            if(!(room.contains(trg.getPosition()))) return false;
        }
        return true;
    }
}
