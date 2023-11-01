package fe.app.controller;

import fe.app.model.elements.map.StreetMap;
import fe.app.model.elements.vehicle.VehicleViewer;
import fe.app.model.elements.map.MapContext;
import fe.app.view.View;

import java.util.Map;

public class Controller {

    private StreetMap streetMap;
    private MapContext mapContext;
    private NetworkController networkController;
    private SensorsController sensorsController;
    private View view;

    public Controller() {
    }

    public void start() {

        this.view = new View();

        this.streetMap = new StreetMap(this);
        this.mapContext = new MapContext(streetMap);

        this.sensorsController = new SensorsController(mapContext);
        this.networkController = new NetworkController(view, sensorsController);

        this.streetMap.setSensorsController(this.sensorsController);
        this.view.setController(this);
        this.view.setNetworkController(this.networkController);

        this.view.start(streetMap);

        VehicleViewer vehicleViewer = new VehicleViewer(view.getMapPanel(), mapContext);
        vehicleViewer.start();

        this.networkController.start();
        this.sensorsController.start();

    }

    public void addVehicle() {
        mapContext.addVehicle();
    }

    public void turnGreen() {
        streetMap.turnGreen();
    }

    public void updateViewTimingsTable(Map<String, Double> timeMap) {
        view.getControlsPanel().updateTimingsTable(timeMap);
    }
}
