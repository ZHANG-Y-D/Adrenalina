package Server.Model;

import java.util.ArrayList;

public class SameBlockConstraint implements RangeConstraint{
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        validSquares.add(shooterPosition);
        return validSquares;
    }
}
