import java.util.ArrayList;

abstract public class TargetsConstraint {
    private boolean specialRange;

    public boolean isSpecialRange(){
        return this.specialRange;
    }

    abstract boolean checkConst(ArrayList<Player> targets);
}
