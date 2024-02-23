package fe.app.controller;

import fe.app.model.elements.map.MapContext;
import fe.app.model.elements.map.MapDimension;
import fe.app.model.elements.vehicle.Vehicle;
import fe.app.util.Pair;

import java.util.ArrayList;

public class TrafficController extends Thread {

    private static final int CAR_SPAWN_MILLIS = 700;
    private int vehiclesNumber;
    private final MapContext mapContext;
    private boolean run = true;

    public TrafficController(int vehiclesNumber, MapContext mapContext) {
        this.vehiclesNumber = vehiclesNumber;
        this.mapContext = mapContext;
    }

    @Override
    public void run() {
        while (run) {
            if (mapContext.getVehicles().size() < this.vehiclesNumber) {
                mapContext.addVehicle();
            }

            for (Vehicle vehicle: new ArrayList<>(mapContext.getVehicles())) {
                if (isOutOfBounds(vehicle.getPosition())) {
                    vehicle.terminate();
                    mapContext.removeVehicle(vehicle);
                }
            }

            try {
                //noinspection BusyWait
                sleep(CAR_SPAWN_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopTraffic() {
        this.run = false;
        this.vehiclesNumber = 0;
    }

    private boolean isOutOfBounds(Pair<Double,Double> position) {
        return position.getX() < 0 || position.getX() > MapDimension.MAP_WIDTH ||
                position.getY() < 0 || position.getY() > MapDimension.MAP_HEIGHT;
    }
}
