package fe.app.model.tfmanagement.server;

import com.google.gson.Gson;

import java.net.Socket;
import java.util.Objects;

public class ServerHandler extends Thread {

    private final Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = Objects.requireNonNull(socket);

    }

    public void run() {
    }
}
