package fe.app.model.tfmanagement.server;

import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimingProcessor {

    private final int semaphoreCycleTime = Semaphore.CYCLE_TIME;
    private final Map<String, Double> timings = new HashMap<>();

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

        Pair<Sensor, Sensor> tempPair = hSensorVehicles >= vSensorVehicles
                ? new Pair<>(hSensor,vSensor)
                : new Pair<>(vSensor,hSensor);

        finalTime = timeAlgorithm(tempPair.getX().getVehiclesNumber() + 1,
                totalVehicles);

        timings.put(tempPair.getX().getSemaphore().getId(), finalTime);
        timings.put(tempPair.getY().getSemaphore().getId(), semaphoreCycleTime - finalTime);

        System.out.println(timings);
    }

    private double timeAlgorithm(int vehicles, int tot) {
        int rawTime = (vehicles * semaphoreCycleTime ) / tot;
        int timeThreshold = 10;
        int timeLimit = semaphoreCycleTime/2 + timeThreshold;

        if (rawTime > timeLimit) {
            int excess = rawTime - timeLimit;
            int weight = vehicles * (1 / tot);

            double maxExcessPercentage = 0.5;
            return (rawTime - excess) + (excess * weight * maxExcessPercentage);
        } else {
            return rawTime;
        }
    }

    public Map<String,Double> getTiming(Pair<String,String> semaphoresIds) {
        Map<String,Double> tempMap = new HashMap<>();
        tempMap.put(semaphoresIds.getX(), timings.get(semaphoresIds.getX()));
        tempMap.put(semaphoresIds.getY(), timings.get(semaphoresIds.getY()));
        return tempMap;
    }

    public Map<String, Double> getTimings() {
        return timings;
    }

}
