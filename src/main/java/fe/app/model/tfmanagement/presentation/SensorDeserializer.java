package fe.app.model.tfmanagement.presentation;

import com.google.gson.*;
import fe.app.model.elements.map.Sensor;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.util.GsonUtils;

import java.lang.reflect.Type;

public class SensorDeserializer implements JsonDeserializer<Sensor>  {
    @Override
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json instanceof JsonObject) {
                var object = json.getAsJsonObject();
                if (object.has("semaphore") && object.has("vehiclesNumber")) {
                    Sensor s = new Sensor(0, 0, GsonUtils.getPropertyAs(object, "semaphore", Semaphore.class, context), null);
                    s.setVehiclesNumber(GsonUtils.getPropertyAsInt(object, "vehiclesNumber"));
                    return s;
                }
            }
            throw new JsonParseException("Invalid semaphore: " + json);
        } catch (ClassCastException exception) {
            throw new JsonParseException("Invalid semaphore: " + json, exception);
        }
    }
}
