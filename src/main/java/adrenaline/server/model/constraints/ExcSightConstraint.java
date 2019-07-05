package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 * This constraint checks whether the squares are not in the sight of the player executing the action.
 */
public class ExcSightConstraint implements RangeConstraint {

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> checkConst(int position, Map map) {
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>(map.getValidSquares(position,1));
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
