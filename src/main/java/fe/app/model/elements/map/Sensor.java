package fe.app.model.elements.map;

import fe.app.model.elements.street.Street;
import fe.app.model.tfmanagement.semaphore.Semaphore;

public class Sensor {

    public static final int DISTANCE_COVERED = 100;
    private final int startingCoordinate;
    private final int endingCoordinate;
    private final Semaphore semaphore;
    private final Street street;
    private int vehiclesNumber;

    public Sensor(int startingCoordinate, int endingCoordinate, Semaphore semaphore, Street street) {
        this.startingCoordinate = startingCoordinate;
        this.endingCoordinate = endingCoordinate;
        this.semaphore = semaphore;
        this.street = street;
    }

    public void setVehiclesNumber(int vehiclesNumber) {
        this.vehiclesNumber = vehiclesNumber;
    }

    public int getStartingCoordinate() {
        return startingCoordinate;
    }

    public int getEndingCoordinate() {
        return endingCoordinate;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public Street getStreet() {
        return street;
    }

    public int getVehiclesNumber() {
        return vehiclesNumber;
    }
}
