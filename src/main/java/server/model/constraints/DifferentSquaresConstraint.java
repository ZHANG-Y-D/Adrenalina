package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;
import java.util.HashSet;


public class DifferentSquaresConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        HashSet<Integer> helper = new HashSet<>();
        for(Player trg : targets){
            if(!helper.add(trg.getPosition())) return false;
        }
        return true;
    }
}
