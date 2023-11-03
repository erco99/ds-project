package fe.app.util;

import com.google.gson.*;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;
import fe.app.model.tfmanagement.presentation.*;
import fe.app.model.tfmanagement.semaphore.Semaphore;

public class GsonUtils {

    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(Request.class, new RequestDeserializer())
                .registerTypeAdapter(SensorsIntersection.class, new SensorsIntersectionSerializer())
                .registerTypeAdapter(SensorsIntersection.class, new SensorsIntersectionDeserializer())
                .registerTypeAdapter(Sensor.class, new SensorSerializer())
                .registerTypeAdapter(Sensor.class, new SensorDeserializer())
                .registerTypeAdapter(Semaphore.class, new SemaphoreSerializer())
                .registerTypeAdapter(Semaphore.class, new SemaphoreDeserializer())
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


    public static Integer getPropertyAsInt(JsonObject object, String name) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return value.getAsInt();
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
