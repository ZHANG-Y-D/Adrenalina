package adrenaline.server.model;

import adrenaline.exceptions.InvalidTargetsException;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;

import java.util.ArrayList;
import java.util.Arrays;

public class Firemode {
    private String name;
    private int[] extraCost;
    private int targetLimit;
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


    public ArrayList<int[]> fire(Player shooter, ArrayList<Player> targets, ArrayList<Integer> validSquares, Map map) throws InvalidTargetsException {
        for(Player trg : targets) {
            if (!validSquares.contains(trg.getPosition())){
                if(targets.indexOf(trg)==0 || trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)) throw new InvalidTargetsException();
            }
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(shooter, targets, map)) throw new InvalidTargetsException();
        }
        //TARGETS VALID
        ArrayList<int[]> returnToEachTarget = new ArrayList<>();
        for(Player trg : targets) {
            returnToEachTarget.add(dmgmrkToEachTarget.get(targets.indexOf(trg) < dmgmrkToEachTarget.size() ? targets.indexOf(trg) : dmgmrkToEachTarget.size()-1));
        }
        return returnToEachTarget;
    }

    @Override
    public String toString() {
        String string = "Firemode{" +
                "name='" + name + '\'' +
                ", extraCost=" + Arrays.toString(extraCost) +
                ", targetLimit=" + targetLimit;
        string += "\n\t\tRange Constraints: ";
        for(RangeConstraint rngConst : rngConstraints){
            string += "\t\t"+ rngConst.getClass().getName() + " ";
        }
        string += "\n\t\tTargets Constraints: ";
        for(TargetsConstraint trgConst : trgConstraints){
            string += "\t\t" + trgConst.getClass().getName() + " ";
        }
        string += "\n\t}";
        return string;
    }
}