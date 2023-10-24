package fe.app.model.elements.map;

import fe.app.model.tfmanagement.semaphore.Semaphore;

public class Sensor {

    public static final int DISTANCE_COVERED = 100;
    private int startingCoordinate;
    private int endingCoordinate;
    private Semaphore semaphore;

    public Sensor(int startingCoordinate, int endingCoordinate, Semaphore semaphore) {
        this.startingCoordinate = startingCoordinate;
        this.endingCoordinate = endingCoordinate;
        this.semaphore = semaphore;
    }
}
