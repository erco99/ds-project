package fe.app.controller;

import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.MapContext;
import fe.app.model.elements.map.Sensor;
import fe.app.model.elements.street.Street;
import fe.app.model.elements.vehicle.Vehicle;
import fe.app.util.StreetType;

import java.util.ArrayList;

public class SensorsController extends Thread {

    private MapContext mapContext;
    private ArrayList<SensorsIntersection> sensorsIntersections;

    public SensorsController(MapContext mapContext) {
        this.mapContext = mapContext;
        this.sensorsIntersections = new ArrayList<>();
    }

    public void addSensorIntersection(SensorsIntersection sensorsIntersection) {
        this.sensorsIntersections.add(sensorsIntersection);
    }

    public void run() {
        while (true) {
            int vSensVehicles = 0;
            int hSensVehicles = 0;
            for (SensorsIntersection sensorIntersection : sensorsIntersections) {
                for (Vehicle vehicle : this.mapContext.getVehicles()) {
                    if (vehicle.getStreet().getType().equals(String.valueOf(StreetType.HORIZONTAL))) {
                        if (isInside(sensorIntersection.getHorizontalStreetSensor(), vehicle.getPosition().getX(), vehicle.getStreet())) {
                            vSensVehicles++;
                        }
                    } else {
                        if (isInside(sensorIntersection.getVerticalStreetSensor(), vehicle.getPosition().getY(), vehicle.getStreet())) {
                            hSensVehicles++;
                        }
                    }
                }
                sensorIntersection.getHorizontalStreetSensor().setVehiclesNumber(vSensVehicles);
                sensorIntersection.getVerticalStreetSensor().setVehiclesNumber(hSensVehicles);

                vSensVehicles = 0;
                hSensVehicles = 0;
            }
            print();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isInside(Sensor sensor, double position, Street street) {
        return position >= sensor.getStartingCoordinate()
                && position <= sensor.getEndingCoordinate()
                && sensor.getStreet() == street;
    }

    private void print() {
        for (SensorsIntersection sensorIntersection : sensorsIntersections) {
            System.out.println("horizontal " + sensorIntersection.getHorizontalStreetSensor().getVehiclesNumber());
            System.out.println("vertical " + sensorIntersection.getVerticalStreetSensor().getVehiclesNumber());

        }
    }
}
