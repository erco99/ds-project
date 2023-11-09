package fe.app.model.elements.vehicle;

import fe.app.model.elements.intersection.StreetsIntersection;
import fe.app.model.elements.intersection.WaysIntersection;
import fe.app.model.elements.map.MapContext;
import fe.app.model.elements.map.StreetMap;
import fe.app.model.tfmanagement.semaphore.SemaphoreState;
import fe.app.model.elements.street.DirectionLine;
import fe.app.model.elements.street.Street;
import fe.app.util.Pair;
import fe.app.util.StreetType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Vehicle extends Thread {

    private static final double SAFETY_DISTANCE = 30;
    private static final int RESTART_TIME_MS = 500;
    private static final double VEHICLE_SPEED = 0.25;
    private Pair<Double,Double> position;
    private boolean terminate;
    private boolean stop;
    private boolean behind = false;
    private MapContext mapContext;
    private StreetMap streetMap;
    private Street street;
    private Street comingStreet;
    private StreetsIntersection streetsIntersection;
    private int intersectionCounter = 0;
    private int xCoef = 0;
    private int yCoef = 0;
    private int direction = 1;
    private Random random = new Random();
    private DirectionLine streetWay;

    public Vehicle(MapContext mapContext, StreetMap streetMap) {
        this.mapContext = mapContext;
        this.streetMap = streetMap;
        this.street = streetMap.getRandomStreet();

        setAngle();

        Pair<Integer,Integer> streetStartingPoint;

        if (random.nextBoolean()) {
            streetWay = street.getRightWay();
            streetStartingPoint = street.getRightWay().getStartingPoint();
        } else {
            streetWay = street.getLeftWay();
            streetStartingPoint = street.getLeftWay().getStartingPoint();
            this.direction *= -1;
        }

        this.position = new Pair<>(Double.valueOf(streetStartingPoint.getX()), Double.valueOf(streetStartingPoint.getY()));
        this.terminate = false;
    }

    public void run() {
        //log("INIT: vel "+vel+"speed "+speed);
        try {
            while (!this.terminate){
                checkIfGo();
                if (!this.stop) {
                    updatePosition();
                }

                // log("pos updated: "+pos);
                Thread.sleep(5);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void updatePosition() {
        this.position = new Pair<>(this.position.getX() + VEHICLE_SPEED * xCoef * direction ,
                this.position.getY() + VEHICLE_SPEED * yCoef * direction);

        if (isInIntersection(this.position)) {
            changeStreet(new Pair<>((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY())));
        }
    }

    private void checkIfGo() throws InterruptedException {
        Pair<Integer,Integer> integerPosition = Pair.toInteger(this.position);

        // check if current position is at a semaphore stop
        // if true, stops if semaphore is red or yellow, goes on otherwise
        // if false, check if there is a vehicle in front of itself
        if (this.streetMap.getSemaphoresPoints().contains(integerPosition)) {
            SemaphoreState currentState = this.streetMap.getSemaphoreByPoint(integerPosition).getCurrentState();
            this.stop = currentState == SemaphoreState.RED || currentState == SemaphoreState.YELLOW;
        } else {
            Pair<Double,Double> frontPosition = new Pair<>(
                    (double)Math.round(this.position.getX() + (VEHICLE_SPEED + SAFETY_DISTANCE) * xCoef * direction),
                    (double)Math.round(this.position.getY() + (VEHICLE_SPEED + SAFETY_DISTANCE) * yCoef * direction)
            );

            if (this.mapContext.getAllPositions().contains(frontPosition)) {
                this.stop = true;
                this.behind = true;
            } else if (this.behind){
                this.stop = false;
                this.behind = false;
                Thread.sleep(RESTART_TIME_MS);
            } else {
                this.stop = false;
            }

        }
    }

    private void changeStreet(Pair<Integer,Integer> position) {
        WaysIntersection waysIntersection = this.streetMap.getWaysIntersectionByPoint(position);

        ArrayList<DirectionLine> intersectedWays = new ArrayList<>();
        intersectedWays.add(waysIntersection.getFirstWay());
        intersectedWays.add(waysIntersection.getSecondWay());

        DirectionLine newStreetWay = intersectedWays.get(random.nextInt(0,2));

        if (this.streetsIntersection == null) {
            this.streetsIntersection = this.streetMap.getStreetsIntersectionByPoint(position);
            this.comingStreet = this.street;
        }

        this.intersectionCounter++;

        if (this.streetsIntersection.equals(this.streetMap.getStreetsIntersectionByPoint(position))) {
            if (this.intersectionCounter == 2) {
                /*if (Objects.equals(this.comingStreet.getType(), String.valueOf(StreetType.HORIZONTAL)) &&
                        Objects.equals(this.streetMap.getStreetById(newStreetWay.getStreetID()).getType(),
                                String.valueOf(StreetType.VERTICAL))) {
                    for (Vehicle vehicle : this.mapContext.getVehicles()) {
                        if (vehicle.getPosition().getX() >= position.getX() &&
                                vehicle.getPosition().getX() < position.getX() + 30) {
                            for (DirectionLine way : intersectedWays) {
                                if (Objects.equals(this.streetMap.getStreetById(way.getStreetID()).getType(),
                                        String.valueOf(StreetType.HORIZONTAL))) {
                                    newStreetWay = way;
                                }
                            }
                            break;
                        }
                    }
                } else if (Objects.equals(this.comingStreet.getType(), String.valueOf(StreetType.VERTICAL)) &&
                        Objects.equals(this.streetMap.getStreetById(newStreetWay.getStreetID()).getType(),
                                String.valueOf(StreetType.HORIZONTAL))) {
                    for (Vehicle vehicle : this.mapContext.getVehicles()) {
                        if (vehicle.getPosition().getY() <= position.getY() &&
                                vehicle.getPosition().getY() > position.getY() - 30) {
                            for (DirectionLine way : intersectedWays) {
                                if (Objects.equals(this.streetMap.getStreetById(way.getStreetID()).getType(),
                                        String.valueOf(StreetType.VERTICAL))) {
                                    newStreetWay = way;
                                }
                            }
                        }
                        break;
                    }
                }*/
            }
            if (this.intersectionCounter == 3) {
                if (Objects.equals(this.comingStreet.getType(), String.valueOf(StreetType.HORIZONTAL)) &&
                        Objects.equals(this.streetMap.getStreetById(newStreetWay.getStreetID()).getType(),
                                String.valueOf(StreetType.HORIZONTAL))) {
                    for (DirectionLine way : intersectedWays) {
                        if (Objects.equals(this.streetMap.getStreetById(way.getStreetID()).getType(),
                                String.valueOf(StreetType.VERTICAL))) {
                            newStreetWay = way;
                            this.intersectionCounter = 0;
                        }
                    }
                } else if (Objects.equals(this.comingStreet.getType(), String.valueOf(StreetType.VERTICAL)) &&
                        Objects.equals(this.streetMap.getStreetById(newStreetWay.getStreetID()).getType(),
                                String.valueOf(StreetType.VERTICAL))) {
                    for (DirectionLine way : intersectedWays) {
                        if (Objects.equals(this.streetMap.getStreetById(way.getStreetID()).getType(),
                                String.valueOf(StreetType.HORIZONTAL))) {
                            newStreetWay = way;
                            this.intersectionCounter = 0;
                        }
                    }
                }
            }
        } else {
            this.intersectionCounter = 1;
            this.comingStreet = this.street;
        }

        this.streetsIntersection = this.streetMap.getStreetsIntersectionByPoint(position);

        if (!Objects.equals(this.streetWay.getDirection(), newStreetWay.getDirection())) {
            this.direction *= -1;
        }
        this.streetWay = newStreetWay;
        this.street = this.streetMap.getStreetById( newStreetWay.getStreetID());

        this.position = Pair.toDouble(position);
        setAngle();
    }

    private void setAngle() {
        if (Objects.equals(this.street.getType(), StreetType.HORIZONTAL.name())) {
            this.yCoef = 0;
            this.xCoef = 1;
        } else {
            this.xCoef = 0;
            this.yCoef = 1;
        }
    }

    private boolean isInIntersection(Pair<Double,Double> point) {
        for (Pair<Integer,Integer> ip : this.streetMap.getStreetIntersectionPoints()) {
            if (Math.abs(point.getX() - ip.getX()) < 0.01 && Math.abs(point.getY() - ip.getY()) < 0.01) {
                return true;
            }
        }
        return false;
    }

    public void terminate() {
        this.terminate = true;
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    public Street getStreet() {
        return street;
    }
}
