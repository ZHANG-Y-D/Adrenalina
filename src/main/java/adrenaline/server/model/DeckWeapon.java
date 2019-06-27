package adrenaline.server.model;

import adrenaline.CustomSerializer;
import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;
import adrenaline.server.model.constraints.TargetsGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class DeckWeapon extends Deck<WeaponCard>{

    public DeckWeapon() {
        try{
            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(RangeConstraint.class, new CustomSerializer())
                    .registerTypeAdapter(TargetsConstraint.class, new CustomSerializer())
                    .registerTypeAdapter(FiremodeSubState.class, new CustomSerializer())
                    .registerTypeAdapter(TargetsGenerator.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            WeaponCard[] weaponCards = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/WeaponCards.json")),WeaponCard[].class);
            cards.addAll(Arrays.asList(weaponCards));
            cards.forEach(x -> x.setLoaded(true));
            shuffle();
        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
    }


    @Override
    public String toString() {
        return "DeckWeapon{" +
                "cardsDeck=" + cards +
                '}';
    }
}
