package fe.app.model.elements;

import fe.app.util.LinesUtil;
import fe.app.util.Pair;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class StreetMap {

    private static final int HORIZONTAL_STREETS_NUMBER = 3;
    private static final int VERTICAL_STREETS_NUMBER = 2;
    private final Random random = new Random();
    private final ArrayList<Street> horizontalStreets;
    private final ArrayList<Street> verticalStreets;
    private final ArrayList<Polygon> intersections;

    public StreetMap() {
        this.horizontalStreets = new ArrayList<>();
        this.verticalStreets = new ArrayList<>();
        this.intersections = new ArrayList<>();
    }

    public void create() {
        int distance = 0;
        for(int i = 0; i < HORIZONTAL_STREETS_NUMBER; i++) {
            int newY = random.nextInt(150, 350);
            this.horizontalStreets.add(new Street("h"+i, 1200, 1000, new Pair<>(0, newY+distance)));
            distance += newY;
        }
        distance = 0;
        for(int i = 0; i < VERTICAL_STREETS_NUMBER; i++) {
            int newX = random.nextInt(200,400);
            this.verticalStreets.add(new Street("v"+i, 1200, 1000, new Pair<>(newX + distance, 0)));
            distance += newX;
        }

        identifyIntersections();
    }

    private void identifyIntersections() {
        for (Street hstreet : this.horizontalStreets) {
            for (Street vstreet: this.verticalStreets) {
                Pair<Integer,Integer> point = LinesUtil.lineLineIntersection(
                    new Pair<>(hstreet.getFirstSide().x1, hstreet.getFirstSide().y1),
                    new Pair<>(hstreet.getFirstSide().x2, hstreet.getFirstSide().y2),
                    new Pair<>(vstreet.getFirstSide().x1, vstreet.getFirstSide().y1),
                    new Pair<>(vstreet.getFirstSide().x2, vstreet.getFirstSide().y2)
                );

                Polygon p = new Polygon();
                p.addPoint(point.getX(), point.getY());
                p.addPoint(point.getX() + Street.ROADWAY_SIZE, point.getY());
                p.addPoint(point.getX() + Street.ROADWAY_SIZE, point.getY() + Street.ROADWAY_SIZE);
                p.addPoint(point.getX(), point.getY() + Street.ROADWAY_SIZE);
                this.intersections.add(p);
            }
        }
    }
    public ArrayList<Street> getHorizontalStreets() {
        return this.horizontalStreets;
    }

    public ArrayList<Street> getVerticalStreets() {
        return verticalStreets;
    }

    public ArrayList<Polygon> getIntersections() {
        return intersections;
    }


}
