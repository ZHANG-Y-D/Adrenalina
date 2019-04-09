package Server.Model;

import java.util.ArrayList;

public class SameBlockConstraint implements RangeConstraint{
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        //TODO
        // add to list only square number corresponding to player's position
        return validSquares;
    }
}
