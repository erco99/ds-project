package fe.app.model.tfmanagement.server;

import fe.app.model.elements.intersection.SensorsIntersection;

import java.util.ArrayList;

public class TimingProcessor {

    public TimingProcessor() {
    }

    public void processTimings(ArrayList<SensorsIntersection> argument) {
        for (SensorsIntersection sensorsIntersection : argument) {
            System.out.println(sensorsIntersection.getVerticalStreetSensor().getVehiclesNumber());
        }
    }

    private void calculateTime(int vSensorVehicles, int hSensorVehicles) {
         
    }

}
