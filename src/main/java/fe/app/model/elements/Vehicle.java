package fe.app.model.elements;

import fe.app.util.Pair;
import fe.app.util.StreetType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Vehicle extends Thread {

    public double vehicleSpeed = 0.16;
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

        this.streetMap.getIntersections().forEach(intersection ->
                this.intersectionPoints.add(intersection.getIntersectionPoint()));
        System.out.println(this.intersectionPoints);

        if (Objects.equals(this.street.getType(), StreetType.HORIZONTAL.name())) {
            this.xCoef = 1;
        } else {
            this.yCoef = 1;
        }
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

        Pair<Integer,Integer> integerPos = Pair.toInteger(this.position);
        if(this.intersectionPoints.contains(integerPos)) {
            this.changeStreet(integerPos);
        }
    }

    private void changeStreet(Pair<Integer,Integer> position) {
        Intersection intersection = this.streetMap.getIntersectionByPoint(position);
        System.out.println(intersection.getIntersectedStreets().getX().getId());
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    public Street getStreet() {
        return street;
    }
}
