package fe.app.model.tfmanagement.server;

import com.google.gson.Gson;
import fe.app.model.tfmanagement.presentation.*;
import fe.app.util.GsonUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class ServerHandler extends Thread {

    private final Socket socket;
    private final Gson gson;
    private final TimingProcessor timingProcessor;

    public ServerHandler(Socket socket, TimingProcessor timingProcessor) {
        this.socket = Objects.requireNonNull(socket);
        this.timingProcessor = timingProcessor;
        this.gson = GsonUtils.createGson();
    }

    public void run() {
        try (socket) {
            var request = unmarshallRequest(socket);
            var response = computeResponse(request);
            marshallResponse(socket, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request<?> unmarshallRequest(Socket socket) throws IOException {
        try {
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            return gson.fromJson(reader, Request.class);
        } finally {
            socket.shutdownInput();
        }
    }

    private Response<?> computeResponse(Request<?> request) {
        String req = gson.toJson(request);
        try {
            switch (request.getMethod()) {
                case "status":
                    return new EmptyResponse(ServerStatus.OK, "Server is UP");
                case "sensors_data":
                    timingProcessor.processTimings(gson.fromJson(req, SensorsData.class).getArgument());
                    return new EmptyResponse(ServerStatus.OK, "Ok");
                case "timings":
                    return new TimingsResponse(
                            ServerStatus.OK,
                            "Timings obtained",
                            timingProcessor.getTiming(gson.fromJson(req, TimingsRequest.class).getArgument()));
                default:
                    return new Response<>();
            }
        } catch (Exception e) {
            return new Response<>(ServerStatus.SERVER_ERROR, e.getMessage());
        }
    }

    private void marshallResponse(Socket socket, Response<?> response) throws IOException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            gson.toJson(response, writer);
            writer.flush();
        } finally {
            socket.shutdownOutput();
        }
    }

}
