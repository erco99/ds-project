package fe.app;

import fe.app.controller.Controller;
import fe.app.controller.NetworkController;
import fe.app.controller.SensorsController;
import fe.app.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new View();

        Controller controller = new Controller(view);
        NetworkController networkController = new NetworkController(view);

        view.getController(controller);
        view.getNetworkController(networkController);

        controller.start();
        networkController.start();
    }
}