package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

public class SameDirectionConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        for(Player trg1 : targets){
            for(Player trg2 : targets){
                if(!map.areAligned(trg1.getPosition(), trg2.getPosition()) ||
                    (trg1.getPosition()> shooter.getPosition() && trg2.getPosition()< shooter.getPosition())||
                        (trg1.getPosition()< shooter.getPosition() && trg2.getPosition()> shooter.getPosition())) return false;
            }
        }
        return true;
    }
}
