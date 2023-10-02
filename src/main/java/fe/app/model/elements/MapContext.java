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

    public ArrayList<Vehicle> getVehicles() {
        return this.vehicles;
    }
}
