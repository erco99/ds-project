package fe.app.controller;

import fe.app.model.elements.VehicleViewer;
import fe.app.model.elements.MapContext;
import fe.app.view.View;

public class Controller {

    View view;

    public Controller(View view) {
        this.view = view;
    }

    public void start() {
        MapContext mapContext = new MapContext();

        view.start();
        VehicleViewer vehicleViewer = new VehicleViewer(view.getMapPanel(), mapContext);
        vehicleViewer.start();


        mapContext.addVehicle();
    }
}
