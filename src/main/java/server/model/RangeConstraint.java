package server.model;

import java.util.ArrayList;

public interface RangeConstraint {
    ArrayList<Integer> checkConst(int shooterPosition, Map map);
}
