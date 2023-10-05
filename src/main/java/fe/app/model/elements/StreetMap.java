package fe.app.model.elements;

import fe.app.util.LinesUtil;
import fe.app.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class StreetMap {

    private static final int HORIZONTAL_STREETS_NUMBER = 3;
    private static final int VERTICAL_STREETS_NUMBER = 2;
    private final Random random = new Random();
    private final ArrayList<Street> horizontalStreets;
    private final ArrayList<Street> verticalStreets;
    private final ArrayList<Polygon> streetSidesIntersections;
    private final ArrayList<StreetsIntersection> streetsIntersections;

    public StreetMap() {
        this.horizontalStreets = new ArrayList<>();
        this.verticalStreets = new ArrayList<>();
        this.streetSidesIntersections = new ArrayList<>();
        this.streetsIntersections = new ArrayList<>();
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
            int newX = random.nextInt(300,500);
            this.verticalStreets.add(new Street("v"+i, 1200, 1000, new Pair<>(newX + distance, 0)));
            distance += newX;
        }

        identifyIntersections();
    }

    private void identifyIntersections() {
        for (Street hStreet : this.horizontalStreets) {
            for (Street vStreet: this.verticalStreets) {
                Line hFirstSide = hStreet.getFirstSide();
                Line vFirstSide = vStreet.getFirstSide();

                DirectionLine hLeftWay = hStreet.getLeftWay();
                DirectionLine hRightWay = hStreet.getRightWay();

                DirectionLine vLeftWay = vStreet.getLeftWay();
                DirectionLine vRightWay = vStreet.getRightWay();

                Pair<Integer,Integer> point = LinesUtil.lineLineIntersection(
                    new Pair<>(hFirstSide.x1, hFirstSide.y1),
                    new Pair<>(hFirstSide.x2, hFirstSide.y2),
                    new Pair<>(vFirstSide.x1, vFirstSide.y1),
                    new Pair<>(vFirstSide.x2, vFirstSide.y2)
                );


                ArrayList<WaysIntersection> intersectionWays = new ArrayList<>();
                intersectionWays.add(new WaysIntersection(getIntersectionPoint(hLeftWay,vLeftWay),hLeftWay,vLeftWay));
                intersectionWays.add(new WaysIntersection(getIntersectionPoint(hLeftWay,vRightWay),hLeftWay,vRightWay));
                intersectionWays.add(new WaysIntersection(getIntersectionPoint(hRightWay,vLeftWay),hRightWay,vLeftWay));
                intersectionWays.add(new WaysIntersection(getIntersectionPoint(hRightWay,vRightWay),hRightWay,vRightWay));

                this.streetsIntersections.add(new StreetsIntersection(intersectionWays, new Pair<>(hStreet,vStreet)));

                Polygon p = new Polygon();
                p.addPoint(point.getX(), point.getY());
                p.addPoint(point.getX() + Street.ROADWAY_SIZE, point.getY());
                p.addPoint(point.getX() + Street.ROADWAY_SIZE, point.getY() + Street.ROADWAY_SIZE);
                p.addPoint(point.getX(), point.getY() + Street.ROADWAY_SIZE);
                this.streetSidesIntersections.add(p);
            }
        }
    }

    private Pair<Integer,Integer> getIntersectionPoint(Line a, Line b) {
        return LinesUtil.lineLineIntersection(
            new Pair<>(a.x1, a.y1),
            new Pair<>(a.x2, a.y2),
            new Pair<>(b.x1, b.y1),
            new Pair<>(b.x2, b.y2)
        );
    }

    public ArrayList<Street> getHorizontalStreets() {
        return this.horizontalStreets;
    }

    public ArrayList<Street> getVerticalStreets() {
        return verticalStreets;
    }

    public ArrayList<Polygon> getStreetSidesIntersections() {
        return streetSidesIntersections;
    }

    public ArrayList<StreetsIntersection> getIntersections() {
        return streetsIntersections;
    }

    public WaysIntersection getWaysIntersectionByPoint(Pair<Integer,Integer> point) {
        for (StreetsIntersection streetsIntersection : this.streetsIntersections) {
            for (WaysIntersection waysIntersection : streetsIntersection.getIntersectionWays())
                if (Pair.equals(waysIntersection.getPoint(), point)) {
                    return waysIntersection;
                }
        }
        return null;
    }

    public StreetsIntersection getStreetsIntersectionByPoint(Pair<Integer,Integer> point) {
        for (StreetsIntersection streetsIntersection : this.streetsIntersections) {
            for (WaysIntersection waysIntersection : streetsIntersection.getIntersectionWays())
                if (Pair.equals(waysIntersection.getPoint(), point)) {
                    return streetsIntersection;
                }
        }
        return null;
    }

    public Street getStreetById(String id) {
        ArrayList<Street> allStreets = new ArrayList<>(horizontalStreets);
        allStreets.addAll(verticalStreets);

        for (Street street : allStreets) {
            if (street.getId() == id) {
                return street;
            }
        }

        return null;
    }

    public Street getRandomStreet() {
        ArrayList<Street> allStreets = new ArrayList<>(this.horizontalStreets);
        allStreets.addAll(this.verticalStreets);
        return allStreets.get(random.nextInt(0, allStreets.size()));
    }
}
