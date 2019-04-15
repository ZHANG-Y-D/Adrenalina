package server.model.constraints;

import server.model.Map;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ExcSightConstraint implements RangeConstraint {
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>(map.getValidSquares(shooterPosition,1));
        ArrayList<Integer> adjacentRooms = new ArrayList<>();
        for(int i : invalidSquares){
            adjacentRooms.addAll(map.getRoomSquares(i));
        }
        invalidSquares.addAll(adjacentRooms);
        invalidSquares = (ArrayList<Integer>) invalidSquares.stream().distinct().collect(Collectors.toList());
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        validSquares.removeAll(invalidSquares);
        return validSquares;
    }
}
