package fe.app.model.elements.map;

import fe.app.controller.SensorsController;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.intersection.StreetsIntersection;
import fe.app.model.elements.intersection.WaysIntersection;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.model.tfmanagement.semaphore.SemaphoreState;
import fe.app.model.elements.street.DirectionLine;
import fe.app.model.elements.street.Line;
import fe.app.model.elements.street.Street;
import fe.app.model.tfmanagement.semaphore.SemaphoresCouple;
import fe.app.util.LinesUtil;
import fe.app.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class StreetMap {

    private static final int HORIZONTAL_STREETS_NUMBER = 3;
    private static final int VERTICAL_STREETS_NUMBER = 2;
    public static final int SEMAPHORE_DISTANCE = 30;
    private final Random random = new Random();
    private final ArrayList<Street> horizontalStreets;
    private final ArrayList<Street> verticalStreets;
    private final ArrayList<Polygon> streetSidesIntersections;
    private final ArrayList<StreetsIntersection> streetsIntersections;
    private ArrayList<Semaphore> semaphores;
    private SensorsController sensorsController;

    public StreetMap() {
        this.horizontalStreets = new ArrayList<>();
        this.verticalStreets = new ArrayList<>();
        this.streetSidesIntersections = new ArrayList<>();
        this.streetsIntersections = new ArrayList<>();
        this.semaphores = new ArrayList<>();
    }

    public void create() {
        int distance = 0;
        for(int i = 0; i < HORIZONTAL_STREETS_NUMBER; i++) {
            int newY = random.nextInt(100, 220);
            this.horizontalStreets.add(new Street("h"+i, MapDimension.MAP_WIDTH, MapDimension.MAP_HEIGHT, new Pair<>(0, newY+distance)));
            distance += newY;
        }
        distance = 0;
        for(int i = 0; i < VERTICAL_STREETS_NUMBER; i++) {
            int newX = random.nextInt(200,400);
            this.verticalStreets.add(new Street("v"+i, MapDimension.MAP_WIDTH, MapDimension.MAP_HEIGHT, new Pair<>(newX + distance, 0)));
            distance += newX;
        }

        identifyIntersections();
        identifySemaphore();
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

                this.streetsIntersections.add(new StreetsIntersection(intersectionWays, hStreet, vStreet));

                Polygon p = new Polygon();
                p.addPoint(point.getX(), point.getY());
                p.addPoint(point.getX() + Street.ROADWAY_SIZE, point.getY());
                p.addPoint(point.getX() + Street.ROADWAY_SIZE, point.getY() + Street.ROADWAY_SIZE);
                p.addPoint(point.getX(), point.getY() + Street.ROADWAY_SIZE);
                this.streetSidesIntersections.add(p);
            }
        }
    }

    private void identifySemaphore() {
        int semaphoreCounter = -1;
        for (StreetsIntersection streetsIntersection : this.streetsIntersections) {
            for (WaysIntersection waysIntersection : streetsIntersection.getIntersectionWays()) {
                if ((Objects.equals(waysIntersection.getFirstWay().getDirection(), "right") &&
                    Objects.equals(waysIntersection.getSecondWay().getDirection(), "right")) ||
                    (Objects.equals(waysIntersection.getSecondWay().getDirection(), "right") &&
                            Objects.equals(waysIntersection.getFirstWay().getDirection(), "right")) ) {
                    Pair<Integer,Integer> point = waysIntersection.getPoint();

                    Semaphore semaphoreOne = new Semaphore(SemaphoreState.RED,
                            new Pair<>(point.getX() - SEMAPHORE_DISTANCE, point.getY()),
                            new Pair<>(point.getX() + Street.STREET_SIDE_DISTANCE + SEMAPHORE_DISTANCE,
                                    point.getY() - Street.STREET_SIDE_DISTANCE),
                            "S" + ++semaphoreCounter);
                    Semaphore semaphoreTwo = new Semaphore(SemaphoreState.RED,
                            new Pair<>(point.getX(), point.getY() - SEMAPHORE_DISTANCE - Street.STREET_SIDE_DISTANCE),
                            new Pair<>(point.getX() + Street.STREET_SIDE_DISTANCE,
                                    point.getY() + SEMAPHORE_DISTANCE),
                            "S" + ++semaphoreCounter);

                    SemaphoresCouple semaphoresCouple = new SemaphoresCouple(semaphoreOne, semaphoreTwo);

                    Sensor sensorHStreet = new Sensor(point.getX() - Sensor.DISTANCE_COVERED,
                            point.getX() + Street.STREET_SIDE_DISTANCE + Sensor.DISTANCE_COVERED,
                            semaphoreOne,
                            streetsIntersection.getHorizontalStreet());

                    Sensor sensorVStreet = new Sensor(point.getY() -
                            Street.STREET_SIDE_DISTANCE - Sensor.DISTANCE_COVERED,
                            point.getY() + Sensor.DISTANCE_COVERED,
                            semaphoreTwo,
                            streetsIntersection.getVerticalStreet());

                    this.semaphores.add(semaphoreOne);
                    this.semaphores.add(semaphoreTwo);

                    this.sensorsController.addSensorIntersection(new SensorsIntersection(sensorHStreet, sensorVStreet));

                    semaphoresCouple.start();
                }
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

    public ArrayList<Semaphore> getSemaphores() {
        return semaphores;
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
            if (Objects.equals(street.getId(), id)) {
                return street;
            }
        }

        return null;
    }

    public Semaphore getSemaphoreByPoint (Pair<Integer,Integer> point) {
        for (Semaphore semaphore : this.semaphores) {
            if (semaphore.getPositions().contains(point)) {
                return semaphore;
            }
        }
        return null;
    }
    public Street getRandomStreet() {
        ArrayList<Street> allStreets = new ArrayList<>(this.horizontalStreets);
        allStreets.addAll(this.verticalStreets);
        return allStreets.get(random.nextInt(0, allStreets.size()));
    }

    public void turnGreen() {
        for (Semaphore semaphore : this.semaphores) {
            semaphore.setCurrentState(SemaphoreState.GREEN);
        }
    }

    public void setSensorsController(SensorsController sensorsController) {
        this.sensorsController = sensorsController;
    }
}
