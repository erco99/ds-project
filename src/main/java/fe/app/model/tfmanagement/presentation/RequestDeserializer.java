package fe.app.model.tfmanagement.presentation;

import com.google.gson.*;
import fe.app.util.GsonUtils;

import java.lang.reflect.Type;

public class RequestDeserializer implements JsonDeserializer<Request<?>> {
    @Override
    public Request<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            if (json instanceof JsonObject) {
                JsonObject object = json.getAsJsonObject();

                if (object.has("method") && object.has("argument")) {
                    return switch (GsonUtils.getPropertyAsString(object, "method")) {
                        case "status" -> new ServerStatusRequest();
                        default -> null;
                    };
                }
                throw new JsonParseException("Invalid request: " + json);
            }
            throw new JsonParseException("Invalid request: " + json);
        } catch (ClassCastException exception) {
            throw new JsonParseException("Invalid request: " + json, exception);
        }

    }
}
