package fe.app.model.tfmanagement.presentation;

import com.google.gson.*;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;
import fe.app.util.GsonUtils;

import java.lang.reflect.Type;

public class SensorsIntersectionDeserializer implements JsonDeserializer<SensorsIntersection> {

    @Override
    public SensorsIntersection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json instanceof JsonObject) {
                var object = json.getAsJsonObject();
                if (object.has("horizontalStreetSensor") && object.has("verticalStreetSensor")) {
                    return new SensorsIntersection(
                            GsonUtils.getPropertyAs(object, "horizontalStreetSensor", Sensor.class, context),
                            GsonUtils.getPropertyAs(object, "verticalStreetSensor", Sensor.class, context)
                    );
                }
            }
            throw new JsonParseException("Invalid sensors intersection: " + json);
        } catch (ClassCastException exception) {
            throw new JsonParseException("Invalid sensors intersection: " + json, exception);
        }
    }
}
