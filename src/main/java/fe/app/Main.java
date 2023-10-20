package fe.app;

import fe.app.controller.Controller;
import fe.app.controller.NetworkController;
import fe.app.view.View;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);

        view.getController(controller);

        controller.start();
        NetworkController networkController = new NetworkController(view);
        view.getNetworkController(networkController);

        try {
            networkController.startConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}