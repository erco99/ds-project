package fe.app.model.tfmanagement.semaphore;

import java.io.IOException;
import java.net.Socket;

public class SemaphoresCouple extends Thread {

    private Semaphore hStreetSemaphore;
    private Semaphore vStreetSemaphore;
    private Socket socket;
    public static final int PORT = 2000;

    public SemaphoresCouple(Semaphore hStreetSemaphore, Semaphore vStreetSemaphore) {
        this.hStreetSemaphore = hStreetSemaphore;
        this.vStreetSemaphore = vStreetSemaphore;
    }

    public void run() {
        try {
            this.socket = new Socket("localhost", PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stateChange() {
        new Thread(() -> {
            while (true) {
                System.out.println("state change");
            }
        }).start();
    }
}
