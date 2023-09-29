package fe.app.model.elements;

import fe.app.util.Pair;

public class DirectionLine extends Line {

    private Pair<Integer, Integer> startingPoint;
    private Pair<Integer, Integer> endingPoint;

    public DirectionLine(Pair<Integer, Integer> startingPoint, Pair<Integer,Integer> endingPoint) {
        super(startingPoint.getX(), startingPoint.getY(), endingPoint.getX(), endingPoint.getY());
    }

    public Pair<Integer, Integer> getStartingPoint() {
        return startingPoint;
    }

    public Pair<Integer, Integer> getEndingPoint() {
        return endingPoint;
    }

}
