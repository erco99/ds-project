package fe.app.model.tfmanagement.presentation;

public class ServerStatusRequest extends Request<ServerStatus> {

    public ServerStatusRequest(ServerStatus argument) {
        super("status", argument);
    }
}
