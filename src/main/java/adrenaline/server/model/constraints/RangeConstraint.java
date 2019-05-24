package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

public interface RangeConstraint {
    ArrayList<Integer> checkConst(int shooterPosition, Map map);
}
