package fe.app.controller;

import fe.app.view.View;

import java.io.IOException;
import java.net.Socket;

public class NetworkController {

    private final View view;
    private Socket socket;
    private int PORT = 2000;

    public NetworkController(View view) {
        this.view = view;
    }

    public void startConnection() throws IOException {
        this.socket = new Socket("localhost", PORT);
        new Thread(() -> {
            while (true) {
                System.out.println("ciao");
            }
        }).start();
    }
}
