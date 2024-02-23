package fe.app.controller;

import fe.app.model.elements.map.StreetMap;
import fe.app.model.elements.vehicle.VehicleViewer;
import fe.app.model.elements.map.MapContext;
import fe.app.view.View;

import java.util.Map;

public class Controller {

    public static final int PORT = 2000;
    private MapContext mapContext;
    private View view;
    private TrafficController trafficController;

    public Controller() {
    }

    public void start() {
         this.view = new View();

        StreetMap streetMap = new StreetMap(this);
        this.mapContext = new MapContext(streetMap);

        SensorsController sensorsController = new SensorsController(mapContext);
        NetworkController networkController = new NetworkController(view, sensorsController, PORT);

        streetMap.setSensorsController(sensorsController);
        this.view.setController(this);
        this.view.setNetworkController(networkController);

        this.view.start(streetMap);

        VehicleViewer vehicleViewer = new VehicleViewer(view.getMapPanel(), mapContext);
        vehicleViewer.start();

        networkController.start();
        sensorsController.start();
    }

    public void startTraffic(int vehiclesNumber) {
        this.trafficController = new TrafficController(vehiclesNumber, mapContext);
        trafficController.start();
    }

    public void stopTraffic() {
        trafficController.stopTraffic();
    }

    public void updateViewTimingsTable(Map<String, Double> timeMap) {
        if (view.getControlsPanel() != null) {
            view.getControlsPanel().updateTimingsTable(timeMap);
        }
    }
}
