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
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/WeaponCards.json");

            WeaponCard[] weaponCards = gson.fromJson(fileReader,WeaponCard[].class);
            cards.addAll(Arrays.asList(weaponCards));
        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
    }


    @Override
    public String toString() {
        return "DeckWeapon{" +
                "cardsDeck=" + cards +
                '}';
    }
}
