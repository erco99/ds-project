package fe.app.controller;

import com.google.gson.Gson;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.tfmanagement.presentation.*;
import fe.app.util.GsonUtils;
import fe.app.view.View;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkController extends Thread {

    private final View view;
    private final SensorsController sensorsController;
    private final Gson gson;
    private int PORT = 2000;
    private final InetSocketAddress socket;
    private boolean isServerUp = true;

    public NetworkController(View view, SensorsController sensorsController) {
        this.view = view;
        this.sensorsController = sensorsController;
        this.socket = new InetSocketAddress("localhost", PORT);
        this.gson = GsonUtils.createGson();
    }

    public void run() {
        while (true) {
            print(sensorsController.getSensorsIntersections());
            this.statusRequest();
            if (isServerUp) {
                this.sendSensorsData();
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendSensorsData() {
        try {
            rpc(new SensorsData(sensorsController.getSensorsIntersections()), EmptyResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void statusRequest() {
        System.out.println("Are you up?");
        try {
            rpc(new ServerStatusRequest(), EmptyResponse.class);
            System.out.println("Server is up");
            isServerUp = true;
        } catch (Exception e) {
            System.out.println("server is down");
            isServerUp = false;
        }
        this.view.getControlsPanel().changeServerStatus(this.isServerUp);
    }

    private <T, R> R rpc(Request<T> request, Class<? extends Response<R>> responseType) throws IOException {
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

    private void print(ArrayList<SensorsIntersection> sensorsIntersections) {
        for (SensorsIntersection sensorIntersection : sensorsIntersections) {
            System.out.println("horizontal " + sensorIntersection.getHorizontalStreetSensor().getVehiclesNumber());
            System.out.println("vertical " + sensorIntersection.getVerticalStreetSensor().getVehiclesNumber());

        }
    }
}
