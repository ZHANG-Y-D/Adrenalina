package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

public class TrajectoryConstraint extends TargetsConstraint {
    private static boolean specialRange = true;

    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) { ;
        for(Player trg : targets) {
            if (!map.areAligned(shooter.getPosition(), trg.getPosition()) ||
                    (shooter.getPosition()>= shooter.getOldPosition() && (trg.getPosition()> shooter.getPosition() || trg.getPosition() < shooter.getOldPosition())) ||
                    (shooter.getPosition()<= shooter.getOldPosition() && (trg.getPosition()< shooter.getPosition() || trg.getPosition() > shooter.getOldPosition()))) return false;
        }
        return true;
    }
}
