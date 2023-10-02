package fe.app.model.elements;

import fe.app.util.Pair;

public class Vehicle extends Thread {

    private Pair<Double,Double> position;
    private boolean stop;
    private MapContext mapContext;
    private StreetMap streetMap;

    public Vehicle(MapContext mapContext, StreetMap streetMap) {
        this.mapContext = mapContext;
        this.streetMap = streetMap;
        Pair<Integer,Integer> f = streetMap.getRandomStreet().getRightWay().getStartingPoint();
        this.position = new Pair<>(Double.valueOf(f.getX()), Double.valueOf(f.getY()));
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
        this.position = new Pair<>(this.position.getX() , this.position.getY() + 0.0036);
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }
}
