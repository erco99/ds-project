package fe.app.model.elements;

import fe.app.util.Pair;

import java.util.ArrayList;

public class VehiclesMonitor {

    private ArrayList<Vehicle> vehicles;

    public VehiclesMonitor() {
        this.vehicles = new ArrayList<>();
    }

    public synchronized void addVehicle(){
        Vehicle agent = new Vehicle(this);
        agent.add(agent);
        agent.start();
    }

    public ArrayList<Pair<Integer, Integer>> getVehiclesPosition() {
        ArrayList<Pair<Integer,Integer>> positions = new ArrayList<>();
        for(Vehicle vehicle : this.vehicles) {
            positions.add(new Pair<>(vehicle.getX(), vehicle.getY()));
        }

        return positions;
    }
}
