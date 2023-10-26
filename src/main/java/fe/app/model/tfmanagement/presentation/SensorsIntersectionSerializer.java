package fe.app.model.tfmanagement.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import fe.app.model.elements.intersection.SensorsIntersection;

import java.lang.reflect.Type;

public class SensorsIntersectionSerializer implements JsonSerializer<SensorsIntersection> {

    @Override
    public JsonElement serialize(SensorsIntersection src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.add("horizontalStreetSensor", context.serialize(src.getHorizontalStreetSensor()));
        object.add("verticalStreetSensor", context.serialize(src.getVerticalStreetSensor()));
        return object;
    }

}
