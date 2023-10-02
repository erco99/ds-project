package fe.app.model.elements;

import fe.app.util.Pair;
import fe.app.util.StreetType;

import java.util.Objects;

public class Vehicle extends Thread {

    private Pair<Double,Double> position;
    private boolean stop;
    private MapContext mapContext;
    private StreetMap streetMap;
    private Street street;
    private int xCoef = 0;
    private int yCoef = 0;

    public Vehicle(MapContext mapContext, StreetMap streetMap) {
        this.mapContext = mapContext;
        this.streetMap = streetMap;
        this.street = streetMap.getRandomStreet();

        if(Objects.equals(this.street.getType(), StreetType.HORIZONTAL.name())) {
            this.xCoef = 1;
        } else {
            this.yCoef = 1;
        }
        Pair<Integer,Integer> streetStartingPoint = street.getRightWay().getStartingPoint();

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
        this.position = new Pair<>(this.position.getX() + 0.036 * xCoef ,
                this.position.getY() + 0.0036 * yCoef);
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    public Street getStreet() {
        return street;
    }
}
