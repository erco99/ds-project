package fe.app.model.elements.vehicle;

import fe.app.model.elements.intersection.StreetsIntersection;
import fe.app.model.elements.intersection.WaysIntersection;
import fe.app.model.elements.map.MapContext;
import fe.app.model.elements.map.StreetMap;
import fe.app.model.elements.street.DirectionLine;
import fe.app.model.elements.street.Street;
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
    private Street comingStreet;
    private StreetsIntersection streetsIntersection;
    private int intersectionCounter = 0;
    private int xCoef = 0;
    private int yCoef = 0;
    private int direction = 1;
    private ArrayList<Pair<Integer,Integer>> intersectionPoints = new ArrayList<>();
    private Random random = new Random();
    private DirectionLine streetWay;

    public Vehicle(MapContext mapContext, StreetMap streetMap) {
        this.mapContext = mapContext;
        this.streetMap = streetMap;
        this.street = streetMap.getRandomStreet();

        this.streetMap.getIntersections().forEach(streetsIntersection ->
                this.intersectionPoints.addAll(streetsIntersection.getAllPoints()));

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
        this.position = new Pair<>(this.position.getX() + vehicleSpeed * xCoef * direction ,
                this.position.getY() + vehicleSpeed * yCoef * direction);

        if (isInIntersection(this.position)) {
            changeStreet(new Pair<>((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY())));
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

    private void changeIfIllegal() {

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


    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    public Street getStreet() {
        return street;
    }
}
