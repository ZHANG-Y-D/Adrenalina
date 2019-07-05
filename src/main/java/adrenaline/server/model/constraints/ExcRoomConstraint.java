package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 *  This constraint checks whether the squares are not in the same room as the player executing the action.
 */
public class ExcRoomConstraint implements RangeConstraint {
    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> checkConst(int position, Map map) {
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>(map.getRoomSquares(position));
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        validSquares.removeAll(invalidSquares);
        return validSquares;
    }
}
