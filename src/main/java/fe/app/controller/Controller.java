package fe.app.controller;

import fe.app.model.elements.StreetMap;
import fe.app.model.elements.VehicleViewer;
import fe.app.model.elements.MapContext;
import fe.app.view.View;

public class Controller {

    View view;

    public Controller(View view) {
        this.view = view;
    }

    public void start() {
        StreetMap streetMap = new StreetMap();
        MapContext mapContext = new MapContext(streetMap);

        view.start(streetMap);
        VehicleViewer vehicleViewer = new VehicleViewer(view.getMapPanel(), mapContext);
        vehicleViewer.start();


        mapContext.addVehicle();
    }
}
