package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

public class ExcRoomConstraint implements RangeConstraint {
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>(map.getRoomSquares(shooterPosition));
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        validSquares.removeAll(invalidSquares);
        return validSquares;
    }
}
