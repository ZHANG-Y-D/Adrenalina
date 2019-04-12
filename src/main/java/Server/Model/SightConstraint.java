package Server.Model;

import java.util.ArrayList;

public class SightConstraint implements RangeConstraint {
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        validSquares.addAll(map.getRoomSquares(shooterPosition));
        for (Integer i : map.getDoorSquares(shooterPosition)) {
            validSquares.addAll(map.getRoomSquares(i));
        }
        return validSquares;
    }
}
