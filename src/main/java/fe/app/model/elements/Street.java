package fe.app.model.elements;

import fe.app.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Street {

    public static final int ROADWAY_SIZE = 40;
    private String id;
    private Line firstSide;
    private Line secondSide;

    public Street(String id, int width, int height, Pair<Integer, Integer> startingPoint) {
        this.id =  id;

        if(startingPoint.getX() == 0) {
            Integer y = startingPoint.getY();
            firstSide = new Line(startingPoint.getX(), y, width, y);
            secondSide = new Line(startingPoint.getX(), y + ROADWAY_SIZE, width, y + ROADWAY_SIZE);
        } else {
            Integer x = startingPoint.getX();
            firstSide = new Line(x, startingPoint.getY(), x, height);
            secondSide = new Line(x + ROADWAY_SIZE, startingPoint.getY(), x + ROADWAY_SIZE, height);
        }
    }

    public Line getFirstSide() {
        return firstSide;
    }

    public Line getSecondSide() {
        return secondSide;
    }

    public String getId() {
        return id;
    }


}
