package fe.app.model.tfmanagement.semaphore;

import fe.app.util.Pair;
import fe.app.util.StreetType;

import java.util.ArrayList;
import java.util.Collections;

public class Semaphore {

    public static final int CYCLE_TIME = 40;
    public static final int YELLOW_TIME = 4;
    private SemaphoreState state;
    private Pair<Integer,Integer> firstSidePosition;
    private Pair<Integer,Integer> secondSidePosition;
    private StreetType streetType;
    private String id;

    public Semaphore(SemaphoreState state,
                     Pair<Integer, Integer> firstSidePosition,
                     Pair<Integer, Integer> secondSidePosition,
                     StreetType streetType, String id) {
        this.state = state;
        this.firstSidePosition = firstSidePosition;
        this.secondSidePosition = secondSidePosition;
        this.streetType = streetType;
        this.id = id;
    }

    public void setCurrentState(SemaphoreState state) {
        this.state = state;
    }

    public SemaphoreState getCurrentState() {
        return state;
    }

    public Pair<Integer, Integer> getFirstSidePosition() {
        return firstSidePosition;
    }

    public Pair<Integer, Integer> getSecondSidePosition() {
        return secondSidePosition;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Pair<Integer,Integer>> getPositions() {
        ArrayList<Pair<Integer,Integer>> positions = new ArrayList<>();
        Collections.addAll(positions, this.firstSidePosition, this.secondSidePosition);
        return positions;
    }

    public StreetType getStreetType() {
        return streetType;
    }
}
