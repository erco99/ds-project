package fe.app.model.tfmanagement.semaphore;

import fe.app.util.Pair;
import fe.app.util.StreetType;

import java.awt.geom.Ellipse2D;

public class SemaphoreShape{

    private final Ellipse2D firstSideShape;
    private final Ellipse2D secondSideShape;
    private static final int SEMAPHORE_DIAMETER = 10;

    public SemaphoreShape(Pair<Integer, Integer> firstSide, Pair<Integer, Integer> secondSide, StreetType streetType) {
        int xShift = 0;
        int yShift = 0;
        if(streetType == StreetType.HORIZONTAL) {
            xShift = 30;
        } else {
            yShift = 30;
        }
        this.firstSideShape = new Ellipse2D.Double(
                firstSide.getX() - SEMAPHORE_DIAMETER / 2 + xShift,
                firstSide.getY() - SEMAPHORE_DIAMETER / 2 + yShift,
                SEMAPHORE_DIAMETER,
                SEMAPHORE_DIAMETER);
        this.secondSideShape = new Ellipse2D.Double(
                secondSide.getX() - SEMAPHORE_DIAMETER/ 2 - xShift,
                secondSide.getY() - SEMAPHORE_DIAMETER / 2 - yShift,
                SEMAPHORE_DIAMETER,
                SEMAPHORE_DIAMETER);
    }

    public Ellipse2D getFirstSideShape() {
        return firstSideShape;
    }

    public Ellipse2D getSecondSideShape() {
        return secondSideShape;
    }
}
