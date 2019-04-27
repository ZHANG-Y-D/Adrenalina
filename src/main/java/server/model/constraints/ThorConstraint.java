package server.model.constraints;

import server.model.Map;
import server.model.PlayerCore;

import java.util.ArrayList;

public class ThorConstraint extends TargetsConstraint {
    private static boolean specialRange = true;

    @Override
    public boolean checkConst(PlayerCore shooter, ArrayList<PlayerCore> targets, Map map) {
        RangeConstraint chainSight = new InSightConstraint();
        for(PlayerCore trg : targets){
            if(targets.indexOf(trg)==0);
            else if(!chainSight.checkConst(targets.get(targets.indexOf(trg)-1).getPosition(),map).contains(trg.getPosition())) return false;

        }
        return true;
    }
}
