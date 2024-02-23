package fe.app.model.elements.intersection;

import fe.app.model.elements.street.DirectionLine;
import fe.app.util.Pair;

public class WaysIntersection {

    private final Pair<Integer,Integer> point;
    private final DirectionLine firstWay;
    private final DirectionLine secondWay;

    public WaysIntersection(Pair<Integer,Integer> point, DirectionLine firstWay, DirectionLine secondWay) {
        this.point = point;
        this.firstWay = firstWay;
        this.secondWay = secondWay;
    }

    public Pair<Integer, Integer> getPoint() {
        return point;
    }

    public DirectionLine getFirstWay() {
        return firstWay;
    }

    public DirectionLine getSecondWay() {
        return secondWay;
    }
}
