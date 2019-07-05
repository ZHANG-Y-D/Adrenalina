package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 * This constraint checks whether every target is in the sight of its preceding one.
 */
public class ThorConstraint implements TargetsConstraint {

    /**
     * {@inheritDoc}
     */
    public boolean isSpecialRange() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        RangeConstraint chainSight = new InSightConstraint();
        for(Player trg : targets){
            if(targets.indexOf(trg)!=0 && !chainSight.checkConst(targets.get(targets.indexOf(trg)-1).getPosition(),map).contains(trg.getPosition())) return false;

        }
        return true;
    }
}
