package fe.app.model.elements;

import fe.app.util.Pair;
import fe.app.util.StreetType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Vehicle extends Thread {

    public double vehicleSpeed = 0.20;
    private Pair<Double,Double> position;
    private boolean stop;
    private MapContext mapContext;
    private StreetMap streetMap;
    private Street street;
    private int xCoef = 0;
    private int yCoef = 0;
    private ArrayList<Pair<Integer,Integer>> intersectionPoints = new ArrayList<>();
    Random random = new Random();

    public Vehicle(MapContext mapContext, StreetMap streetMap) {
        this.mapContext = mapContext;
        this.streetMap = streetMap;
        this.street = streetMap.getRandomStreet();


        this.streetMap.getIntersections().forEach(streetsIntersection ->
                this.intersectionPoints.addAll(streetsIntersection.getAllPoints()));

        setDirection();

        Pair<Integer,Integer> streetStartingPoint;

        if (random.nextBoolean()) {
            streetStartingPoint = street.getRightWay().getStartingPoint();
        } else {
            streetStartingPoint = street.getLeftWay().getStartingPoint();
            this.vehicleSpeed *= -1;
        }

        this.position = new Pair<>(Double.valueOf(streetStartingPoint.getX()), Double.valueOf(streetStartingPoint.getY()));
        this.stop = false;
    }

    public void run() {
        //log("INIT: vel "+vel+"speed "+speed);
        try {
            while (!this.stop){
                updatePosition();
                // log("pos updated: "+pos);
                Thread.sleep(5);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void terminate() {
        this.stop = true;
    }

    private void updatePosition() {
        this.position = new Pair<>(this.position.getX() + vehicleSpeed * xCoef ,
                this.position.getY() + vehicleSpeed * yCoef);

        if (isInIntersection(this.position)) {
            changeStreet(new Pair<>((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY())));
        }
    }

    private void changeStreet(Pair<Integer,Integer> position) {
        StreetsIntersection streetsIntersection = this.streetMap.getIntersectionByPoint(position);
        ArrayList<Street> intersectedStreets = new ArrayList<>();
        intersectedStreets.add(streetsIntersection.getIntersectionStreets().getX());
        intersectedStreets.add(streetsIntersection.getIntersectionStreets().getY());

        this.street = intersectedStreets.get(random.nextInt(0,2));
        this.position = Pair.toDouble(position);
        setDirection();
    }

    private void setDirection() {
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

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    public Street getStreet() {
        return street;
    }
}
