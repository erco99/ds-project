package fe.app.model.elements;

import fe.app.util.Pair;

public class Semaphore {

    private SemaphoreState state;
    private Pair<Integer,Integer> firstSidePosition;
    private Pair<Integer,Integer> secondSidePosition;

    public Semaphore(SemaphoreState state, Pair<Integer, Integer> firstSidePosition, Pair<Integer, Integer> secondSidePosition) {
        this.state = state;
        this.firstSidePosition = firstSidePosition;
        this.secondSidePosition = secondSidePosition;
    }
}
