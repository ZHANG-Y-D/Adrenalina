package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class SameRoomConstraint implements TargetsConstraint, TargetsGenerator {
    @Override
    public boolean isSpecialRange() {
        return false;
    }

    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        ArrayList<Integer> room = map.getRoomSquares(targets.get(0).getPosition());
        for(Player trg : targets){
            if(!(room.contains(trg.getPosition()))) return false;
        }
        return true;
    }

    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        Set<Integer> validSquares = new LinkedHashSet<Integer>();
        validSquares.add(root);
        validSquares.addAll(map.getRoomSquares(root));
        return new ArrayList<>(validSquares);
    }
}
