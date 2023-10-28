package fe.app.model.tfmanagement.presentation;

import com.google.gson.*;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.util.GsonUtils;
import fe.app.util.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class RequestDeserializer implements JsonDeserializer<Request<?>> {
    @Override
    public Request<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            if (json instanceof JsonObject) {
                JsonObject object = json.getAsJsonObject();

                if (object.has("method") && object.has("argument")) {
                    return switch (Objects.requireNonNull(GsonUtils.getPropertyAsString(object, "method"))) {
                        case "status" -> new ServerStatusRequest();
                        case "sensors_data" -> new SensorsData(
                                GsonUtils.getPropertyAs(object, "argument", ArrayList.class, context)
                        );
                        case "timings" -> new TimingsRequest(
                                GsonUtils.getPropertyAs(object, "argument", Pair.class, context)
                        );
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
