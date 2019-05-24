package adrenaline.server.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomDeserializer implements JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String type = jsonObj.get("type").getAsString();
        JsonElement object = jsonObj.get("properties");
        try{
            return context.deserialize(object, Class.forName(type));
        }
        catch(ClassNotFoundException cnf){
            throw new JsonParseException("Unknown element type", cnf);
        }
    }
}
