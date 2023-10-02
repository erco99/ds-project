package fe.app.model.elements;

import fe.app.util.Pair;

import java.util.ArrayList;

public class MapContext {

    private ArrayList<Vehicle> vehicles;
    private StreetMap streetMap;

    public MapContext(StreetMap streetMap) {
        this.vehicles = new ArrayList<>();
        this.streetMap = streetMap;
    }

    public synchronized void addVehicle(){
        Vehicle agent = new Vehicle(this, streetMap);
        this.vehicles.add(agent);
        agent.start();
    }

    public ArrayList<Pair<Double, Double>> getVehiclesPosition() {
        ArrayList<Pair<Double,Double>> positions = new ArrayList<>();
        for(Vehicle vehicle : this.vehicles) {
            positions.add(new Pair<>(vehicle.getPosition().getX(), vehicle.getPosition().getY()));
        }

        return positions;
    }
}
