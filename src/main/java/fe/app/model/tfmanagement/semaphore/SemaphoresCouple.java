package fe.app.model.tfmanagement.semaphore;

import com.google.gson.Gson;
import fe.app.controller.Controller;
import fe.app.model.tfmanagement.client.ClientHandler;
import fe.app.model.tfmanagement.presentation.*;
import fe.app.util.GsonUtils;
import fe.app.util.Pair;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.Map;

public class SemaphoresCouple extends Thread {

    private Semaphore hStreetSemaphore;
    private Semaphore vStreetSemaphore;
    private InetSocketAddress socket;
    public static final int PORT = 2000;
    private ClientHandler clientHandler;
    private final Gson gson;
    private Map<String, Double> timeMap;
    private Controller controller;
    private String mode = "autonomous";

    public SemaphoresCouple(Semaphore hStreetSemaphore, Semaphore vStreetSemaphore, Controller controller) {
        this.hStreetSemaphore = hStreetSemaphore;
        this.vStreetSemaphore = vStreetSemaphore;
        this.socket = new InetSocketAddress("localhost", PORT);
        this.gson = GsonUtils.createGson();
        this.controller = controller;
    }

    public void run() {
        this.clientHandler = new ClientHandler(this.socket, this.gson);
        try {
            timeMap = clientHandler.rpc(
                    new TimingsRequest(new Pair<>(hStreetSemaphore.getId(), vStreetSemaphore.getId())),
                    TimingsResponse.class);
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        }
        if (timeMap.containsValue(null)) {
            timeMap.put(hStreetSemaphore.getId(), 20.0);
            timeMap.put(vStreetSemaphore.getId(), 20.0);
        }
        startStateCycle();
        startTimingsRequests();
    }

    private void startTimingsRequests() {
        try {
            while (true) {
                sendTimingsRequest();
                sleep(50000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimingsRequest() {
        try {
            timeMap = clientHandler.rpc(
                    new TimingsRequest(new Pair<>(hStreetSemaphore.getId(), vStreetSemaphore.getId())),
                    TimingsResponse.class);
            mode = "remote";
        } catch (IllegalStateException | ConnectException e) {
            mode = "autonomous";
            timeMap.put(hStreetSemaphore.getId(), 20.0);
            timeMap.put(vStreetSemaphore.getId(), 20.0);
        }

        if (timeMap.containsValue(null)) {
            timeMap.put(hStreetSemaphore.getId(), 20.0);
            timeMap.put(vStreetSemaphore.getId(), 20.0);
        }

        try {
            controller.updateViewTimingsTable(timeMap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void startStateCycle() {
        new Thread(() -> {
            while (true) {
                hStreetSemaphore.setCurrentState(SemaphoreState.GREEN);
                vStreetSemaphore.setCurrentState(SemaphoreState.RED);
                try {
                    sleep((long) (timeMap.get(hStreetSemaphore.getId()) * 1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                hStreetSemaphore.setCurrentState(SemaphoreState.YELLOW);
                try {
                    sleep(Semaphore.YELLOW_TIME * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                hStreetSemaphore.setCurrentState(SemaphoreState.RED);
                vStreetSemaphore.setCurrentState(SemaphoreState.GREEN);
                try {
                    sleep((long) (timeMap.get(vStreetSemaphore.getId()) * 1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                vStreetSemaphore.setCurrentState(SemaphoreState.YELLOW);
                try {
                    sleep(Semaphore.YELLOW_TIME * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public Map<String, Double> getTimeMap() {
        return timeMap;
    }

    public String getMode() {
        return mode;
    }


}
