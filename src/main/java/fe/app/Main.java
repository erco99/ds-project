package fe.app;

import fe.app.controller.Controller;
import fe.app.controller.NetworkController;
import fe.app.view.View;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();

        controller.start();
    }
}