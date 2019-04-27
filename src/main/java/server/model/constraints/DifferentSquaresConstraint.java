package server.model.constraints;

import server.model.Map;
import server.model.PlayerCore;

import java.util.ArrayList;
import java.util.HashSet;


public class DifferentSquaresConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(PlayerCore shooter, ArrayList<PlayerCore> targets, Map map) {
        HashSet<Integer> helper = new HashSet<>();
        for(PlayerCore trg : targets){
            if(!helper.add(trg.getPosition())) return false;
        }
        return true;
    }
}
