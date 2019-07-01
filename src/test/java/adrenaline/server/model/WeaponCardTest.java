package adrenaline.server.model;


import adrenaline.CustomSerializer;
import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.model.constraints.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;


class WeaponCardTest {
    @Test
    void WeaponCardModelerFromJson(){
        try {
            FileReader fileReader = new FileReader("src/test/testResource/testJsonsrc/TestWeaponCard.json");

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

    @Test
    void firemodeDeepCopyTest(){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("src/test/testResource/testJsonsrc/TestWeaponCard.json");
        } catch (FileNotFoundException e) { }
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(RangeConstraint.class, new CustomSerializer())
                .registerTypeAdapter(TargetsConstraint.class, new CustomSerializer())
                .registerTypeAdapter(TargetsGenerator.class, new CustomSerializer())
                .registerTypeAdapter(FiremodeSubState.class, new CustomSerializer());
        WeaponCard jsonWeapon = gsonBld.create().fromJson(fileReader, WeaponCard.class);
        Firemode copied = jsonWeapon.getFiremode(0);
    }

}