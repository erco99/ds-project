package fe.app.model.elements.intersection;

import fe.app.model.elements.street.Street;
import fe.app.util.Pair;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreetsIntersection that)) return false;
        return Objects.equals(intersectionWays, that.intersectionWays) && Objects.equals(intersectionStreets, that.intersectionStreets);
    }
}
