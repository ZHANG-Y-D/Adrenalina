package server.model.constraints;

import server.model.Map;
import server.model.PlayerCore;

import java.util.ArrayList;

public class SameSquareConstraint extends TargetsConstraint {
    private static boolean specialRange = false;

    @Override
    public boolean checkConst(PlayerCore shooter, ArrayList<PlayerCore> targets, Map map) {
        Integer square = targets.get(0).getPosition();
        for(PlayerCore trg : targets){
            if(square!=trg.getPosition()) return false;
        }
        return true;
    }
}
