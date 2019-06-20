package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;
import java.util.HashSet;


public class DifferentSquaresConstraint implements TargetsConstraint {
    public boolean isSpecialRange() {
        return false;
    }

    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        HashSet<Integer> helper = new HashSet<>();
        for(Player trg : targets){
            if(!helper.add(trg.getPosition())) return false;
        }
        return true;
    }
}
