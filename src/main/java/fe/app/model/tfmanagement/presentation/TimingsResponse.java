package fe.app.model.tfmanagement.presentation;

import fe.app.util.Pair;

import java.util.Map;

public class TimingsResponse extends Response<Map<String,Double>> {

    public TimingsResponse(ServerStatus status, String message, Map<String,Double> result) {
        super(status, message, result);
    }
}
