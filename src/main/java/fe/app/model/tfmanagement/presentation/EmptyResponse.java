package fe.app.model.tfmanagement.presentation;

public class EmptyResponse extends Response<Void> {

    public EmptyResponse() {
    }

    public EmptyResponse(ServerStatus status, String message) {
        super(status, message);
    }
}
