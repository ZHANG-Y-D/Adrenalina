package server.model.constraints;

import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

abstract public class TargetsConstraint {
    private static boolean specialRange;

    public boolean isSpecialRange(){
        return this.specialRange;
    }

    public abstract boolean checkConst(Player shooter, ArrayList<Player> targets, Map map);
}
