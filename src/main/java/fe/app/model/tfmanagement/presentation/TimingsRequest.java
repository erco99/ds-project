package fe.app.model.tfmanagement.presentation;

import fe.app.util.Pair;

public class TimingsRequest extends Request<Pair<String,String>> {

    public TimingsRequest(Pair<String,String> argument) {
        super("timings", argument);
    }
}
