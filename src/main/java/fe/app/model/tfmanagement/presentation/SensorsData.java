package fe.app.model.tfmanagement.presentation;

import fe.app.model.elements.intersection.SensorsIntersection;

import java.util.ArrayList;

public class SensorsData extends Request<ArrayList<SensorsIntersection>> {

    public SensorsData(ArrayList<SensorsIntersection> argument) {
        super("sensors_data", argument);
    }
}
