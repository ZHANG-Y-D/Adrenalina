package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;


import java.util.ArrayList;

public class ChargeConstraint implements TargetsConstraint {
    public boolean isSpecialRange() {
        return true;
    }

    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        if(!map.areAligned(shooter.getPosition(), shooter.getOldPosition())) return false;
        for(Player trg : targets) {
            if (!map.areAligned(shooter.getOldPosition(), trg.getPosition()) || (!map.areAligned(shooter.getPosition(), trg.getPosition())) ||
                    (shooter.getPosition()>= shooter.getOldPosition() && (trg.getPosition()> shooter.getPosition() || trg.getPosition() < shooter.getOldPosition())) ||
                    (shooter.getPosition()<= shooter.getOldPosition() && (trg.getPosition()< shooter.getPosition() || trg.getPosition() > shooter.getOldPosition()))) return false;
        }
        return true;
    }
}
