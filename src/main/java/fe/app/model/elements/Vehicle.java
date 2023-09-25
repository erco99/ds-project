package fe.app.model.elements;

import fe.app.util.Pair;

public class Vehicle extends Thread {

    private Pair<Double,Double> position;
    private boolean stop;

    private MapContext mapContext;

    public Vehicle(MapContext mapContext) {
        this.mapContext = mapContext;
        this.position = new Pair<>(0.0,0.0);
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
        this.position = new Pair<>(this.position.getX() , this.position.getY() + 0.00036);
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }


}
