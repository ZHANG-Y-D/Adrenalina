package server.model.constraints;

import server.model.Map;

import java.util.ArrayList;

public interface RangeConstraint {
    ArrayList<Integer> checkConst(int shooterPosition, Map map);
}
