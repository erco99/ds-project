package fe.app.model.elements;

import fe.app.util.Pair;

public class WaysIntersection {

    private Pair<Integer,Integer> point;
    private DirectionLine leftWay;
    private DirectionLine rightWay;

    public WaysIntersection(Pair<Integer,Integer> point, DirectionLine leftWay, DirectionLine rightWay) {
        this.point = point;
        this.leftWay = leftWay;
        this.rightWay = rightWay;
    }

    public Pair<Integer, Integer> getPoint() {
        return point;
    }

    public DirectionLine getLeftWay() {
        return leftWay;
    }

    public DirectionLine getRightWay() {
        return rightWay;
    }
}
