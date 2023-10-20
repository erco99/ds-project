package fe.app.util;

import com.google.gson.*;
import fe.app.model.tfmanagement.presentation.Request;
import fe.app.model.tfmanagement.presentation.RequestDeserializer;

public class GsonUtils {

    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(Request.class, new RequestDeserializer())
                .create();
    }

    public static String getPropertyAsString(JsonObject object, String name) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return value.getAsString();
        }
        return null;
    }

    public static <T> T getPropertyAs(JsonObject object, String name, Class<T> type,  JsonDeserializationContext context) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return context.deserialize(value, type);
        }
        return null;
    }
}
