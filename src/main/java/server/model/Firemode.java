package server.model;

import server.InvalidTargetsException;
import server.model.constraints.*;

import java.util.ArrayList;

public class Firemode {
    private String name;
    private int[] extraCost;
    private int targetLimit; // value 0 used as flag for area target firemodes
    private ArrayList<MovementEffect> mvEffects;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsConstraint> trgConstraints;
    private ArrayList<int[]> dmgmrkToEachTarget;

    public Firemode(String name, int[] extraCost, int targetLimit, ArrayList<MovementEffect> mvEff, ArrayList<RangeConstraint> rngConst, ArrayList<TargetsConstraint> trgConst, ArrayList<int[]> dmgmrk){
        this.name = name;
        this.extraCost = extraCost;
        this.targetLimit = targetLimit;
        this.mvEffects = mvEff;
        this.rngConstraints = rngConst;
        this.trgConstraints = trgConst;
        this.dmgmrkToEachTarget = dmgmrk;
    }

    public int[] getExtraCost(){
        return extraCost;
    }
    public int getTargetLimit() { return targetLimit;}

    public ArrayList<MovementEffect> getMovementEffects() { return mvEffects; }

    public ArrayList<Integer> getRange(int shooterPosition, Map map){
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        for(int i = 0; i<= map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        for(RangeConstraint rc : rngConstraints){
            validSquares.retainAll(rc.checkConst(shooterPosition, map));
        }
        return validSquares;
    }


    public ArrayList<int[]> fire(ArrayList<Player> targets, ArrayList<Integer> validSquares, Map map) throws InvalidTargetsException {
        for(Player trg : targets) {
            if (!validSquares.contains(trg.getPosition())){
                if(targets.indexOf(trg)==0 || trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)) throw new InvalidTargetsException();
            }
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(targets, map)) throw new InvalidTargetsException();
        }
        //TARGETS VALID
        ArrayList<int[]> returnToEachTarget = new ArrayList<>();
        for(Player trg : targets) {
            returnToEachTarget.add(dmgmrkToEachTarget.get(targets.indexOf(trg) < dmgmrkToEachTarget.size() ? targets.indexOf(trg) : dmgmrkToEachTarget.size()-1));
        }
        return returnToEachTarget;
    }

}