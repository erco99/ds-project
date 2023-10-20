package fe.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
}
