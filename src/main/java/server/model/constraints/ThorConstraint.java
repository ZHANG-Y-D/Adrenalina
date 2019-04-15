package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

public class ThorConstraint extends TargetsConstraint {
    private static boolean specialRange = true;

    @Override
    public boolean checkConst(ArrayList<Player> targets, Map map) {
        RangeConstraint chainSight = new InSightConstraint();
        for(Player trg : targets){
            if(targets.indexOf(trg)==0);
            else if(!chainSight.checkConst(targets.get(targets.indexOf(trg)-1).getPosition(),map).contains(trg.getPosition())) return false;

        }
        return true;
    }
}
