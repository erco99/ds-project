package fe.app.model.elements;

import fe.app.util.Pair;

import java.util.ArrayList;

public class StreetsIntersection {

    private ArrayList<WaysIntersection> intersectionWays;
    private Pair<Street, Street> intersectionStreets;

    public StreetsIntersection(ArrayList<WaysIntersection> intersectionWays, Pair<Street, Street> intersectionStreets) {
        this.intersectionWays = intersectionWays;
        this.intersectionStreets = intersectionStreets;
    }

    public ArrayList<WaysIntersection> getIntersectionWays() {
        return intersectionWays;
    }

    public Pair<Street, Street> getIntersectionStreets() {
        return intersectionStreets;
    }

    public ArrayList<Pair<Integer,Integer>> getAllPoints() {
        ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
        intersectionWays.forEach(i ->
                points.add(i.getPoint())
        );

        return points;
    }
}
