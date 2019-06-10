package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

abstract public class TargetsConstraint {
    protected static boolean specialRange;

    public boolean isSpecialRange(){
        return specialRange;
    }

    public abstract boolean checkConst(Player shooter, ArrayList<Player> targets, Map map);
}
