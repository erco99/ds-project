package fe.app.model.elements.semaphore;

import fe.app.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Semaphore {

    private SemaphoreState state;
    private Pair<Integer,Integer> firstSidePosition;
    private Pair<Integer,Integer> secondSidePosition;
    private String id;

    public Semaphore(SemaphoreState state,
                     Pair<Integer, Integer> firstSidePosition,
                     Pair<Integer, Integer> secondSidePosition,
                     String id) {
        this.state = state;
        this.firstSidePosition = firstSidePosition;
        this.secondSidePosition = secondSidePosition;
        this.id = id;
    }

    public void setState(SemaphoreState state) {
        this.state = state;
    }

    public SemaphoreState getState() {
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
}
