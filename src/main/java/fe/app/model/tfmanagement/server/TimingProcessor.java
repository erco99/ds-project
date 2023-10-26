package fe.app.model.tfmanagement.server;

import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;
import fe.app.model.tfmanagement.semaphore.Semaphore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimingProcessor {

    private final int semaphoreCycleTime = Semaphore.CYCLE_TIME;
    private final int timeThreshold = 10;
    private final double maxExcessPerc = 0.5;
    private Map<String, Double> timings = new HashMap<>();

    public TimingProcessor() {
    }

    public void processTimings(ArrayList<SensorsIntersection> argument) {
        for (SensorsIntersection sensorsIntersection : argument) {
            calculateGreenTime(sensorsIntersection.getHorizontalStreetSensor(),
                    sensorsIntersection.getVerticalStreetSensor());
        }
    }

    private void calculateGreenTime(Sensor hSensor, Sensor vSensor) {
        int hSensorVehicles = hSensor.getVehiclesNumber() + 1;
        int vSensorVehicles = vSensor.getVehiclesNumber() + 1;

        int totalVehicles = vSensorVehicles + hSensorVehicles;
        double finalTime;

        if (hSensorVehicles >= vSensorVehicles) {
            finalTime = timeAlgorithm(hSensorVehicles, totalVehicles);

            System.out.println("tempo hsensor: " + finalTime);
            System.out.println("tempo vsensor: " + (semaphoreCycleTime - finalTime));

            timings.put(hSensor.getSemaphore().getID(), finalTime);
        } else {
            finalTime = timeAlgorithm(vSensorVehicles, totalVehicles);

            System.out.println("tempo vsensor: " + finalTime);
            System.out.println("tempo hsensor: " + (semaphoreCycleTime - finalTime));

            timings.put(vSensor.getSemaphore().getID(), finalTime);
        }
    }

    private  double timeAlgorithm(int vehicles, int tot) {
        int rawTime = (vehicles * semaphoreCycleTime ) / tot;
        int timeLimit = semaphoreCycleTime/2 + timeThreshold;

        if (rawTime > timeLimit) {
            int excess = rawTime - timeLimit;
            int weight = vehicles * (1 / tot);

            return (rawTime - excess) + (excess * weight * maxExcessPerc);
        } else {
            return rawTime;
        }
    }

}
