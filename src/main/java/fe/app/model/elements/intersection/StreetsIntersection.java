package fe.app.model.elements.intersection;

import fe.app.model.elements.street.Street;
import fe.app.util.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class StreetsIntersection {

    private ArrayList<WaysIntersection> intersectionWays;
    private Street horizontalStreet;
    private Street verticalStreet;

    public StreetsIntersection(ArrayList<WaysIntersection> intersectionWays, Street horizontalStreet, Street verticalStreet) {
        this.intersectionWays = intersectionWays;
        this.horizontalStreet = horizontalStreet;
        this.verticalStreet = verticalStreet;
    }

    public ArrayList<WaysIntersection> getIntersectionWays() {
        return intersectionWays;
    }


    public ArrayList<Pair<Integer,Integer>> getAllPoints() {
        ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
        intersectionWays.forEach(i ->
                points.add(i.getPoint())
        );

        return points;
    }

    public Street getHorizontalStreet() {
        return horizontalStreet;
    }

    public Street getVerticalStreet() {
        return verticalStreet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreetsIntersection that)) return false;

        if (!Objects.equals(intersectionWays, that.intersectionWays))
            return false;
        if (!Objects.equals(horizontalStreet, that.horizontalStreet))
            return false;
        return Objects.equals(verticalStreet, that.verticalStreet);
    }
}
