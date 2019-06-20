package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

public interface TargetsConstraint {
    boolean isSpecialRange();
    boolean checkConst(Player shooter, ArrayList<Player> targets, Map map);
}
