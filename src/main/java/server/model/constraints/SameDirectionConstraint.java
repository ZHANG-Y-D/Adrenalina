package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

public class SameDirectionConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(ArrayList<Player> targets, Map map) {
        for(Player trg1 : targets){
            for(Player trg2 : targets){
                if(!map.areAligned(trg1.getPosition(), trg2.getPosition())) return false;
            }
        }
        return true;
    }
}
