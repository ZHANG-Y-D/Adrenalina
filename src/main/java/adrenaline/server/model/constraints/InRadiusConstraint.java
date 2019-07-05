package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;
import java.util.Set;

/**
 * {@inheritDoc}
 * This constraint checks whether the squares are within a specific radius of the player executing the action.
 */
public class InRadiusConstraint implements RangeConstraint, TargetsGenerator{
    private int radius;

    /**
     * Constructor for specifying the radius
     */
    public InRadiusConstraint(int radius){
        this.radius = radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> checkConst(int position, Map map) {
        return new ArrayList<>(map.getValidSquares(position,radius));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        ArrayList<Integer> validSquares = map.getValidSquares(shooterPos,radius);
        validSquares.remove(shooterPos);
        return validSquares;
    }
}
