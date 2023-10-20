package fe.app.controller;

import com.google.gson.Gson;
import fe.app.model.tfmanagement.presentation.Request;
import fe.app.util.GsonUtils;
import fe.app.view.View;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class NetworkController {

    private final View view;
    private Socket socket;
    private final Gson gson;
    private int PORT = 2000;

    public NetworkController(View view) {
        this.view = view;
        gson = GsonUtils.createGson();
    }

    public void startConnection() throws IOException {
        this.socket = new Socket("localhost", PORT);
        new Thread(() -> {
            while (true) {

            }
        }).start();
    }


    private <T> void marshallRequest(Socket socket, Request<T> request) throws IOException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            gson.toJson(request, writer);
            writer.flush();
        } finally {
            socket.shutdownOutput();
        }
    }

    private void sendStatusRequest() {

    }

}
