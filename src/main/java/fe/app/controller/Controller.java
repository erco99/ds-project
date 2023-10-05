package fe.app.controller;

import fe.app.model.elements.map.StreetMap;
import fe.app.model.elements.vehicle.VehicleViewer;
import fe.app.model.elements.map.MapContext;
import fe.app.view.View;

public class Controller {

    private final View view;
    private StreetMap streetMap;
    private MapContext mapContext;

    public Controller(View view) {
        this.view = view;
    }

    public void start() {
        this.streetMap = new StreetMap();
        mapContext = new MapContext(streetMap);

        view.start(streetMap);
        VehicleViewer vehicleViewer = new VehicleViewer(view.getMapPanel(), mapContext);
        vehicleViewer.start();

        mapContext.addVehicle();
    }

    public void addVehicle() {
        mapContext.addVehicle();
    }
}
