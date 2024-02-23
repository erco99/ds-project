package fe.app.model.elements.street;

import fe.app.model.elements.street.Line;
import fe.app.util.Pair;

public class DirectionLine extends Line {

    private final Pair<Integer, Integer> startingPoint;
    private final Pair<Integer, Integer> endingPoint;
    private final String streetID;

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

    public String getDirection() {
        if (startingPoint.getX() == 0 || startingPoint.getY() == 0) {
            return "right";
        } else {
            return "left";
        }
    }
}
