package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.model.constraints.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeaponCardTest {
    @Test
    void WeaponCardModelerFromJson(){
        try {
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/TestWeaponCard.json");

            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(RangeConstraint.class, new CustomSerializer())
                    .registerTypeAdapter(TargetsConstraint.class, new CustomSerializer())
                    .registerTypeAdapter(FiremodeSubState.class, new CustomSerializer())
                    .registerTypeAdapter(TargetsGenerator.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            WeaponCard jsonWeapon = gson.fromJson(fileReader, WeaponCard.class);

            System.out.println(jsonWeapon.toString());
        }catch(FileNotFoundException e){System.out.println("ERROR!");}
    }

    /*
    @Test
    void weaponCardModelerTest(){
        WeaponCard weapon;
        Firemode fm;
        ArrayList<Firemode> fmList = new ArrayList<>();
        ArrayList<MovementEffect> mvEffects = new ArrayList<>();
        ArrayList<RangeConstraint> rngConstraints = new ArrayList<>();
        ArrayList<TargetsConstraint> trgConstraints = new ArrayList<>();
        ArrayList<int[]> dmgmrk = new ArrayList<>();

        /* LOCK RIFLE *//*
        rngConstraints.add(new InSightConstraint());
        dmgmrk.add(new int[]{2,1});
        fm = new Firemode("Basic", new int[]{0,0,0}, 1, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.add(new int[]{0,1});
        fm = new Firemode("Second Lock", new int[]{1,0,0}, 2, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Lock Rifle", new int[]{0,2,0}, Color.BLACK, "", 0, fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* ELECTROSCYTHE *//*
        rngConstraints.add(new InRadiusConstraint(0));
        dmgmrk.add(new int[]{1,0});
        fm = new Firemode("Basic", new int[]{0,0,0}, 0, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.set(0, new int[]{2,0});
        fm = new Firemode("Reaper Mode", new int[]{1,1,0}, 0, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Electroscythe", new int[]{0,1,0}, Color.BLACK, "", 0,  fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* MACHINE GUN *//*
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
        weapon = new WeaponCard("Machine gun", new int[]{1,1,0}, Color.BLACK, "", 0,  fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* TRACTOR BEAM *//*
        mvEffects.add(new MovementEffect(2, false, false, MovementEffect.Timing.PRE));
        rngConstraints.add(new InSightConstraint());
        dmgmrk.add(new int[]{1,0});
        fm = new Firemode("Basic", new int[]{0,0,0}, 1, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        rngConstraints.set(0, new InRadiusConstraint(0));
        dmgmrk.set(0, new int[]{3,0});
        fm = new Firemode("Punisher Mode", new int[]{1,0,1}, 1, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Tractor beam", new int[]{0,1,0}, Color.BLACK, "", 0,  fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* THOR *//*
        rngConstraints.add(new InSightConstraint());
        dmgmrk.add(new int[]{2,0});
        fm = new Firemode("Basic", new int[]{0,0,0}, 1, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.add(new int[]{1,0});
        trgConstraints.add(new ThorConstraint());
        fm = new Firemode("Chain Reaction", new int[]{0,1,0}, 2, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        dmgmrk.add(new int[]{2,0});
        fm = new Firemode("High Voltage", new int[]{0,2,0}, 3, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Thor", new int[]{1,1,0}, Color.BLACK, "", 0,  fmList);

        mvEffects.clear();
        rngConstraints.clear();
        trgConstraints.clear();
        dmgmrk.clear();

        /* POWER GLOVE *//*
        mvEffects.add(new MovementEffect(1, true, true , MovementEffect.Timing.PRE));
        rngConstraints.add(new InRadiusConstraint(0));
        dmgmrk.add(new int[]{1,2});
        fm = new Firemode("Basic", new int[]{0,0,0}, 1, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        mvEffects.set(0, new MovementEffect(2, true, true, MovementEffect.Timing.PRE));
        rngConstraints.add(new InRadiusConstraint(0));
        trgConstraints.add(new ChargeConstraint());
        dmgmrk.set(0, new int[]{2,0});
        fm = new Firemode("Rocket fist", new int[]{0,1,0}, 2, mvEffects, rngConstraints, trgConstraints, dmgmrk);
        fmList.add(fm);
        weapon = new WeaponCard("Power glove", new int[]{0,1,0}, Color.BLACK, "", 0,  fmList);
    }
    */

}