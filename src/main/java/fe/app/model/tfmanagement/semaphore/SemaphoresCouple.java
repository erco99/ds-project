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

    public SemaphoresCouple(Semaphore hStreetSemaphore, Semaphore vStreetSemaphore) {
        this.hStreetSemaphore = hStreetSemaphore;
        this.vStreetSemaphore = vStreetSemaphore;
        this.socket = new InetSocketAddress("localhost", PORT);
        this.gson = GsonUtils.createGson();
    }

    public void run() {
        this.clientHandler = new ClientHandler(this.socket, this.gson);
        startStateCycle();

        while (true) {
            sendTimingsRequest();
        }

    }

    private void sendTimingsRequest() {
        try {
            while (true) {
                Map<String, Double> prova = clientHandler.rpc(
                        new TimingsRequest(new Pair<>(hStreetSemaphore.getID(), vStreetSemaphore.getID())),
                        TimingsResponse.class);
                System.out.println(prova);
                sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startStateCycle() {
        new Thread(() -> {
            while (true) {
            }
        }).start();
    }
}
