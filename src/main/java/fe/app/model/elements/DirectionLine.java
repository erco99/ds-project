package fe.app.model.elements;

import fe.app.util.Pair;

public class DirectionLine extends Line {

    private Pair<Integer, Integer> startingPoint;
    private Pair<Integer, Integer> endingPoint;
    private String streetID;

    public DirectionLine(String streetID, Pair<Integer, Integer> startingPoint, Pair<Integer,Integer> endingPoint) {
        super(startingPoint.getX(), startingPoint.getY(), endingPoint.getX(), endingPoint.getY());
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.streetID = streetID;
    }

    public Pair<Integer, Integer> getStartingPoint() {
        return startingPoint;
    }

    public Pair<Integer, Integer> getEndingPoint() {
        return endingPoint;
    }

    public String getStreetID() {
        return streetID;
    }


}
