package adrenaline;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomSerializer implements JsonDeserializer<Object>, JsonSerializer<Object> {

    @Override
    public JsonElement serialize(Object o, Type typeOfT, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", o.getClass().getName());
        jsonObject.add("properties", context.serialize(o));
        return jsonObject;
    }

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
