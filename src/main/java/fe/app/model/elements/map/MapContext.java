package fe.app.model.elements.map;

import fe.app.model.elements.vehicle.Vehicle;
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

    public ArrayList<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public ArrayList<Pair<Double, Double>> getAllPositions() {
        ArrayList<Pair<Double, Double>> positions = new ArrayList<>();
        for (Vehicle vehicle : new ArrayList<>(this.vehicles)) {
            double x = (double)Math.round(vehicle.getPosition().getX());
            double y= (double)Math.round(vehicle.getPosition().getY());
            positions.add(new Pair<>(x,y));
        }

        return positions;
    }

    public void removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
    }
}
