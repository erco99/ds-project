package fe.app.controller;

import com.google.gson.Gson;
import fe.app.model.tfmanagement.client.ClientHandler;
import fe.app.model.tfmanagement.presentation.*;
import fe.app.util.GsonUtils;
import fe.app.view.View;

import java.net.InetSocketAddress;

public class NetworkController extends Thread {

    private final View view;
    private final SensorsController sensorsController;
    private final Gson gson;
    private int PORT = 2000;
    private final InetSocketAddress socket;

    private boolean isServerUp = true;
    private ClientHandler clientHandler;

    public NetworkController(View view, SensorsController sensorsController) {
        this.view = view;
        this.sensorsController = sensorsController;
        this.socket = new InetSocketAddress("localhost", PORT);
        this.gson = GsonUtils.createGson();
    }

    public void run() {
        this.clientHandler = new ClientHandler(this.socket, this.gson);
        while (true) {
            this.statusRequest();
            if (isServerUp) {
                this.sendSensorsData();
                this.view.getControlsPanel().setSensorsLabel(sensorsController.getSensorsIntersections());
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
            clientHandler.rpc(new SensorsData(sensorsController.getSensorsIntersections()), EmptyResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void statusRequest() {
        System.out.println("Are you up?");
        try {
            clientHandler.rpc(new ServerStatusRequest(), EmptyResponse.class);
            System.out.println("Server is up");
            isServerUp = true;
        } catch (Exception e) {
            System.out.println("server is down");
            isServerUp = false;
        }
        this.view.getControlsPanel().changeServerStatus(this.isServerUp);
    }

    public boolean isServerUp() {
        return isServerUp;
    }

}
