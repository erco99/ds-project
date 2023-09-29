package fe.app.model.elements;

import fe.app.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Street {

    public static final int ROADWAY_SIZE = 42;
    private static final int STREETSIDE_DISTANCE = 14;
    private String id;
    private Line firstSide;
    private Line secondSide;
    private DirectionLine rightWay;
    private DirectionLine leftWay;

    public Street(String id, int width, int height, Pair<Integer, Integer> startingPoint) {
        this.id =  id;

        if(startingPoint.getX() == 0) {
            Integer y = startingPoint.getY();

            firstSide = new Line(startingPoint.getX(), y, width, y);
            leftWay = new DirectionLine(new Pair<>(width, y + STREETSIDE_DISTANCE),
                    new Pair<>(startingPoint.getX(), y + STREETSIDE_DISTANCE));

            secondSide = new Line(startingPoint.getX(), y + ROADWAY_SIZE, width, y + ROADWAY_SIZE);
            rightWay = new DirectionLine(new Pair<>(width, y + ROADWAY_SIZE - STREETSIDE_DISTANCE),
                    new Pair<>(startingPoint.getX(), y + ROADWAY_SIZE - STREETSIDE_DISTANCE));
        } else {
            Integer x = startingPoint.getX();

            firstSide = new Line(x, startingPoint.getY(), x, height);
            leftWay = new DirectionLine(new Pair<>(x + STREETSIDE_DISTANCE, startingPoint.getY()),
                    new Pair<>(x + STREETSIDE_DISTANCE, height));

            secondSide = new Line(x + ROADWAY_SIZE, startingPoint.getY(), x + ROADWAY_SIZE, height);
            rightWay = new DirectionLine(new Pair<>(x + ROADWAY_SIZE - STREETSIDE_DISTANCE, startingPoint.getY()),
                    new Pair<>(x + ROADWAY_SIZE - STREETSIDE_DISTANCE, height));

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

    public String getId() {
        return id;
    }


}
