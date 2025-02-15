package fe.app.model.tfmanagement.presentation;

import com.google.gson.*;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.util.GsonUtils;

import java.lang.reflect.Type;

public class SemaphoreDeserializer implements JsonDeserializer<Semaphore> {

    @Override
    public Semaphore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json instanceof JsonObject) {
                var object = json.getAsJsonObject();
                if (object.has("ID")) {
                    return new Semaphore(
                            null,
                            null,
                            null,
                            null,
                            GsonUtils.getPropertyAsString(object, "ID")
                    );
                }
            }
            throw new JsonParseException("Invalid semaphore: " + json);
        } catch (ClassCastException exception) {
            throw new JsonParseException("Invalid semaphore: " + json, exception);
        }
    }
}
