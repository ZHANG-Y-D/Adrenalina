package adrenaline;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 *
 * For Serializer
 *
 */
public class CustomSerializer implements JsonDeserializer<Object>, JsonSerializer<Object> {

    /**
     * For serialize object
     * @param o The object to be serialized
     * @param typeOfT The type of Object
     * @param context The Json Serialization Context
     * @return
     */
    @Override
    public JsonElement serialize(Object o, Type typeOfT, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", o.getClass().getName());
        jsonObject.add("properties", context.serialize(o));
        return jsonObject;
    }

    /**
     *
     *
     * For deserialize the object
     * @param jsonElement The json element to be deserialize
     * @param typeOfT The type of the object to be deserialize
     * @param context The Json Deserialization Context
     * @return The object from deserialize
     * @throws JsonParseException To throws Json Pars eException
     */
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
