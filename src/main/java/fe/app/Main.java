package fe.app;

import fe.app.controller.Controller;
import fe.app.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);

        controller.start();
    }
}