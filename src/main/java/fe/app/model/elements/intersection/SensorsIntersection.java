package fe.app.model.elements.intersection;

import fe.app.model.elements.map.Sensor;

public class SensorsIntersection {

    private Sensor horizontalStreetSensor;
    private Sensor verticalStreetSensor;

    public SensorsIntersection(Sensor horizontalStreetSensor, Sensor verticalStreetSensor) {
        this.horizontalStreetSensor = horizontalStreetSensor;
        this.verticalStreetSensor = verticalStreetSensor;
    }

    public Sensor getHorizontalStreetSensor() {
        return horizontalStreetSensor;
    }

    public Sensor getVerticalStreetSensor() {
        return verticalStreetSensor;
    }
}
