package fe.app.model.elements;

import fe.app.util.Pair;

public class WaysIntersection {

    private Pair<Integer,Integer> point;
    private DirectionLine firstWay;
    private DirectionLine secondWay;

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
