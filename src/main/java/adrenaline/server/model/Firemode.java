package adrenaline.server.model;

import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.controller.states.MoveSelfState;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;

import java.util.*;

/**
 *
 * The firemdoe class made for all firemode
 *
 *
 */
public class Firemode {

    private int[] extraCost;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsConstraint> trgConstraints;
    private Queue<FiremodeSubState> firemodeSteps = new LinkedList<>();
    private int allowedMovement;

    /**
     *
     * The getter of the extraCost
     *
     * @return The array of extraCost
     */
    public int[] getExtraCost(){
        return extraCost;
    }

    /**
     *
     * To get the Next Step,It will retrieves and removes the head of firemodeSteps's queue
     *
     * @return The FiremodeSubState reference
     */
    public FiremodeSubState getNextStep(){ return firemodeSteps.poll(); }

    /**
     *
     * To get move self step
     *
     * @return A reference of MoveSelfState
     */
    public MoveSelfState getMoveSelfStep() {
        if(allowedMovement>0){
            int movement = allowedMovement;
            allowedMovement=0;
            return new MoveSelfState(movement);
        }else return null;
    }

    /**
     *
     * To get range of this action
     *
     * @param shooterPosition The shooter's Position
     * @param map A reference of map
     * @return An Integer aArrayList of Range
     */
    public ArrayList<Integer> getRange(int shooterPosition, Map map){
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        for(int i = 0; i<= map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        rngConstraints.forEach(x -> validSquares.retainAll(x.checkConst(shooterPosition, map)));
        return validSquares;
    }

    /**
     *
     * To check targets
     *
     * @param shooter The reference of shooter
     * @param targets A Player ArrayList
     * @param map A reference of map
     * @return True if this action is ok for game rules
     */
    public boolean checkTargets(Player shooter, ArrayList<Player> targets, Map map) {
        ArrayList<Integer> validSquares = getRange(shooter.getPosition(), map);
        for(Player trg : targets) {
            if (!validSquares.contains(trg.getPosition()) && (targets.indexOf(trg)==0 || trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)))
                return false;
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(shooter, targets, map)) return false;
        }
        return true;
    }
}