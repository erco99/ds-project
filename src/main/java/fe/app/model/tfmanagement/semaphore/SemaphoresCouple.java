package fe.app.model.tfmanagement.semaphore;

import com.google.gson.Gson;
import fe.app.model.tfmanagement.client.ClientHandler;
import fe.app.model.tfmanagement.presentation.*;
import fe.app.util.GsonUtils;
import fe.app.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

public class SemaphoresCouple extends Thread {

    private Semaphore hStreetSemaphore;
    private Semaphore vStreetSemaphore;
    private InetSocketAddress socket;
    public static final int PORT = 2000;
    private ClientHandler clientHandler;
    private final Gson gson;
    Map<String, Double> timeMap;

    public SemaphoresCouple(Semaphore hStreetSemaphore, Semaphore vStreetSemaphore) {
        this.hStreetSemaphore = hStreetSemaphore;
        this.vStreetSemaphore = vStreetSemaphore;
        this.socket = new InetSocketAddress("localhost", PORT);
        this.gson = GsonUtils.createGson();
    }

    public void run() {
        this.clientHandler = new ClientHandler(this.socket, this.gson);
        timeMap = clientHandler.rpc(
                new TimingsRequest(new Pair<>(hStreetSemaphore.getID(), vStreetSemaphore.getID())),
                TimingsResponse.class);
        if (timeMap.containsValue(null)) {
            timeMap.put(hStreetSemaphore.getID(), 20.0);
            timeMap.put(vStreetSemaphore.getID(), 20.0);
        }
        startStateCycle();
        startTimingsRequests();
    }

    private void startTimingsRequests() {
        try {
            while (true) {
                sendTimingsRequest();
                sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimingsRequest() {
        timeMap = clientHandler.rpc(
                new TimingsRequest(new Pair<>(hStreetSemaphore.getID(), vStreetSemaphore.getID())),
                TimingsResponse.class);
    }

    private void startStateCycle() {
        new Thread(() -> {
            while (true) {
                hStreetSemaphore.setCurrentState(SemaphoreState.GREEN);
                vStreetSemaphore.setCurrentState(SemaphoreState.RED);
                try {
                    sleep((long) (timeMap.get(hStreetSemaphore.getID()) * 1000));
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
                    sleep((long) (timeMap.get(vStreetSemaphore.getID()) * 1000));
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
}
