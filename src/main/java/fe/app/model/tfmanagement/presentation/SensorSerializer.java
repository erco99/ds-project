package fe.app.model.tfmanagement.presentation;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import fe.app.model.elements.map.Sensor;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class SensorSerializer implements JsonSerializer<Sensor> {

    @Override
    public JsonElement serialize(Sensor src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.add("semaphore", context.serialize(src.getSemaphore()));
        object.addProperty("vehiclesNumber", src.getVehiclesNumber());
        return object;
    }

}
