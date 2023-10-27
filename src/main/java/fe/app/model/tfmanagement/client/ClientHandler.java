package fe.app.model.tfmanagement.client;

import com.google.gson.Gson;
import fe.app.model.tfmanagement.presentation.Request;
import fe.app.model.tfmanagement.presentation.Response;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientHandler {
    private final InetSocketAddress socket;
    private final Gson gson;

    public ClientHandler(InetSocketAddress socket, Gson gson) {
        this.socket = socket;
        this.gson = gson;
    }

    public <T, R> R rpc(Request<T> request, Class<? extends Response<R>> responseType) {
        try (var socket = new Socket()) {
            socket.connect(this.socket);
            marshallRequest(socket, request);
            return unmarshallResponse(socket, responseType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
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

    private <T> T unmarshallResponse(Socket socket, Class<? extends Response<T>> responseType) throws IOException {
        try {
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            Response<T> response = responseType.cast(gson.fromJson(reader, responseType));

            return switch (response.getStatus()) {
                case OK -> response.getResult();
                case SERVER_ERROR -> throw new Error(response.getMessage());
            };
        } finally {
            socket.shutdownInput();
        }
    }
}
