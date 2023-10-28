package fe.app.model.tfmanagement.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import fe.app.model.elements.map.Sensor;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.model.tfmanagement.semaphore.SemaphoresCouple;

import java.lang.reflect.Type;

public class SemaphoreSerializer implements JsonSerializer<Semaphore> {

    @Override
    public JsonElement serialize(Semaphore src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("ID", src.getID());
        return object;
    }
}
