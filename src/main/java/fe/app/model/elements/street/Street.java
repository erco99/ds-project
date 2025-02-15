package fe.app.model.elements.street;

import fe.app.util.Pair;
import fe.app.util.StreetType;

public class Street {

    public static final int ROADWAY_SIZE = 42;
    public static final int STREET_SIDE_DISTANCE = 14;
    private final String id;
    private final Line firstSide;
    private final Line secondSide;
    private final DirectionLine rightWay;
    private final DirectionLine leftWay;
    private final StreetType streetType;

    public Street(String id, int width, int height, Pair<Integer, Integer> startingPoint) {
        this.id =  id;

        if(startingPoint.getX() == 0) {
            Integer y = startingPoint.getY();
            this.streetType = StreetType.HORIZONTAL;

            firstSide = new Line(startingPoint.getX(), y, width, y);
            leftWay = new DirectionLine(this.id, new Pair<>(width, y + STREET_SIDE_DISTANCE),
                    new Pair<>(startingPoint.getX(), y + STREET_SIDE_DISTANCE));

            secondSide = new Line(startingPoint.getX(), y + ROADWAY_SIZE, width, y + ROADWAY_SIZE);
            rightWay = new DirectionLine(this.id, new Pair<>(0, y + ROADWAY_SIZE - STREET_SIDE_DISTANCE),
                    new Pair<>(width, y + ROADWAY_SIZE - STREET_SIDE_DISTANCE));
        } else {
            Integer x = startingPoint.getX();
            this.streetType = StreetType.VERTICAL;

            firstSide = new Line(x, startingPoint.getY(), x, height);
            rightWay = new DirectionLine(this.id, new Pair<>(x + STREET_SIDE_DISTANCE, 0),
                    new Pair<>(x + STREET_SIDE_DISTANCE, height));

            secondSide = new Line(x + ROADWAY_SIZE, startingPoint.getY(), x + ROADWAY_SIZE, height);
            leftWay = new DirectionLine(this.id, new Pair<>(x + ROADWAY_SIZE - STREET_SIDE_DISTANCE, height),
                    new Pair<>(x + ROADWAY_SIZE - STREET_SIDE_DISTANCE, startingPoint.getY()));
        }
    }

    public Line getFirstSide() {
        return firstSide;
    }

    public Line getSecondSide() {
        return secondSide;
    }

    public DirectionLine getRightWay() {
        return rightWay;
    }

    public DirectionLine getLeftWay() {
        return leftWay;
    }

    public StreetType getType() {
        return streetType;
    }
    public String getId() {
        return id;
    }
}
