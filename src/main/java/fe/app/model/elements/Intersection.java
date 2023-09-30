package fe.app.model.elements;

import fe.app.util.Pair;

public class Intersection {

    private Pair<Integer,Integer> intersectionPoint;
    private Pair<Street, Street> intersectedStreets;

    public Intersection(Pair<Integer, Integer> intersectionPoint, Pair<Street, Street> intersectedStreets) {
        this.intersectionPoint = intersectionPoint;
        this.intersectedStreets = intersectedStreets;
    }

    public Pair<Integer, Integer> getIntersectionPoint() {
        return intersectionPoint;
    }

    public Pair<Street, Street> getIntersectedStreets() {
        return intersectedStreets;
    }
}
