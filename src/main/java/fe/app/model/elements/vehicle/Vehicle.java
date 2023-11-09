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

    public static final double SAFETY_DISTANCE = 30;
    public static final int RESTART_TIME = 500;
    public double vehicleSpeed = 0.25;
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
    private ArrayList<Pair<Integer,Integer>> intersectionPoints = new ArrayList<>();
    private ArrayList<Pair<Integer,Integer>> semaphorePoints = new ArrayList<>();
    private Random random = new Random();
    private DirectionLine streetWay;

    public Vehicle(MapContext mapContext, StreetMap streetMap) {
        this.mapContext = mapContext;
        this.streetMap = streetMap;
        this.street = streetMap.getRandomStreet();

        this.streetMap.getIntersections().forEach(streetsIntersection ->
                this.intersectionPoints.addAll(streetsIntersection.getAllPoints()));

        this.streetMap.getSemaphores().forEach(semaphore -> {
            this.semaphorePoints.addAll(semaphore.getPositions());
        });

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
        this.position = new Pair<>(this.position.getX() + vehicleSpeed * xCoef * direction ,
                this.position.getY() + vehicleSpeed * yCoef * direction);

        //checkFront();

        if (isInIntersection(this.position)) {
            changeStreet(new Pair<>((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY())));
        }
    }

    private void checkIfGo() throws InterruptedException {
        if (this.semaphorePoints.contains(Pair.toInteger(this.position)) &&
                ((this.streetMap.getSemaphoreByPoint(Pair.toInteger(this.position)).getCurrentState() == SemaphoreState.RED) ||
                (this.streetMap.getSemaphoreByPoint(Pair.toInteger(this.position)).getCurrentState() == SemaphoreState.YELLOW))) {
            this.stop = true;
        } else if (this.semaphorePoints.contains(Pair.toInteger(this.position)) &&
                this.streetMap.getSemaphoreByPoint(Pair.toInteger(this.position)).getCurrentState() == SemaphoreState.GREEN) {
            this.stop = false;
        } else {
            Pair<Double,Double> prova = new Pair<>(this.position.getX() + (vehicleSpeed + SAFETY_DISTANCE) * xCoef * direction,
                    this.position.getY() + (vehicleSpeed + SAFETY_DISTANCE) * yCoef * direction);
            double x = (double)Math.round(prova.getX());
            double y= (double)Math.round(prova.getY());

            if (this.mapContext.getAllPositions().contains(new Pair<>(x, y))) {
                this.stop = true;
                this.behind = true;
            } else if (this.behind){
                this.stop = false;
                this.behind = false;
                Thread.sleep(RESTART_TIME);
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
/*            if (this.intersectionCounter == 2) {
                if (Objects.equals(this.comingStreet.getType(), String.valueOf(StreetType.HORIZONTAL)) &&
                Objects.equals(this.streetMap.getStreetById(newStreetWay.getStreetID()).getType(),
                        String.valueOf(StreetType.VERTICAL))) {
                    for (Vehicle vehicle : this.mapContext.getVehicles()) {
                        if (vehicle.getPosition().getX() < position.getX() &&
                            vehicle.getPosition().getX() > position.getX() + 30) {
                            this.stop = true;
                        } else {
                            this.stop = false;
                        }
                    }
                }
            }*/
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
        for (Pair<Integer,Integer> ip : this.intersectionPoints) {
            if (Math.abs(point.getX() - ip.getX()) < 0.01 && Math.abs(point.getY() - ip.getY()) < 0.01) {
                return true;
            }
        }
        return false;
    }

    private void checkFront() {
        Pair<Double,Double> prova = new Pair<>(this.position.getX() + (vehicleSpeed + SAFETY_DISTANCE) * xCoef * direction,
                this.position.getY() + (vehicleSpeed + SAFETY_DISTANCE) * yCoef * direction);
        double x = (double)Math.round(prova.getX());
        double y= (double)Math.round(prova.getY());

        if (this.mapContext.getAllPositions().contains(new Pair<>(x,y))) {
            System.out.println("stop");
            this.stop = true;
        }
    }

    public void terminate() {
        this.terminate = true;
    }

    public void block() {
        this.stop = true;
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    public Street getStreet() {
        return street;
    }
}
