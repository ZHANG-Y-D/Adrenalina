package server.model;

import org.junit.jupiter.api.Test;
import server.model.constraints.InRadiusConstraint;
import server.model.constraints.InSightConstraint;
import server.model.constraints.RangeConstraint;
import server.model.constraints.TargetsConstraint;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WeaponCardTest {
    @Test
    void weaponCardModelerTest(){
        WeaponCard weapon;
        Firemode fm;
        ArrayList<Firemode> fmList = new ArrayList<>();
        ArrayList<MovementEffect> mvEffects = new ArrayList<>();
        ArrayList<RangeConstraint> rngConstraints = new ArrayList<>();
        ArrayList<TargetsConstraint> trgConstraints = new ArrayList<>();
        ArrayList<int[]> dmgmrk = new ArrayList<>();

        /* LOCK RIFLE */
        rngConstraints.add(new InSightConstraint());
        dmgmrk.add(new int[]{2,1});
        fm = new Firemode("Basic", new int[]{0,0,0}, 1, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.add(new int[]{0,1});
        fm = new Firemode("Second Lock", new int[]{1,0,0}, 2, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Lock Rifle", new int[]{0,2,0}, fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* ELECTROSCYTHE */
        rngConstraints.add(new InRadiusConstraint(0));
        dmgmrk.add(new int[]{1,0});
        fm = new Firemode("Basic", new int[]{0,0,0}, 0, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.set(0, new int[]{2,0});
        fm = new Firemode("Reaper Mode", new int[]{1,1,0}, 0, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Electroscythe", new int[]{0,1,0}, fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* MACHINE GUN */
        rngConstraints.add(new InSightConstraint());
        dmgmrk.add(new int[]{1,0});
        fm = new Firemode("Basic", new int[]{0,0,0}, 2, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.set(0, new int[]{2,0});
        dmgmrk.add(new int[]{1,0});
        fm = new Firemode("Focus shot", new int[]{0,0,1}, 2, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        fm = new Firemode("Turret tripod", new int[]{0,1,0}, 3, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.set(1, new int[]{2,0});
        dmgmrk.add(new int[]{1,0});
        fm = new Firemode("Turret tripod + Focus shot", new int[]{0,1,1}, 3, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Machine gun", new int[]{1,1,0}, fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

    }
}