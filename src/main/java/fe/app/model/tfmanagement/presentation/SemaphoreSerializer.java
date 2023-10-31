package fe.app.model.tfmanagement.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import fe.app.model.tfmanagement.semaphore.Semaphore;

import java.lang.reflect.Type;

public class SemaphoreSerializer implements JsonSerializer<Semaphore> {

    @Override
    public JsonElement serialize(Semaphore src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("ID", src.getId());
        return object;
    }
}
