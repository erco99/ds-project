import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;
import fe.app.model.elements.map.StreetMap;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.model.tfmanagement.semaphore.SemaphoreState;
import fe.app.model.tfmanagement.semaphore.SemaphoresCouple;
import fe.app.model.tfmanagement.server.Server;
import fe.app.model.tfmanagement.server.TimingProcessor;
import fe.app.util.Pair;
import fe.app.util.StreetType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestSystem {
    public static int port = 2000;
    private static StreetMap streetMap;
    private static Server server;

    @BeforeAll
    public static void beforeAll() {
        streetMap = new StreetMap(null);
    }

    @BeforeEach
    public void beforeEach() {
        port++;
    }

    @Test
    public void testTimeMap() {
        Semaphore semaphoreOne = new Semaphore(SemaphoreState.RED,
                new Pair<>(4,4),
                new Pair<>(5,5),
                StreetType.HORIZONTAL,
                "S" + 1);
        Semaphore semaphoreTwo = new Semaphore(SemaphoreState.RED,
                new Pair<>(6,6),
                new Pair<>(7,7),
                StreetType.VERTICAL,
                "S" +2);

        TimingProcessor timingProcessor = new TimingProcessor();
        SensorsIntersection sensorsIntersection = new SensorsIntersection(new Sensor(2,3,semaphoreOne,null),
                new Sensor(3,4,semaphoreTwo,null));
        sensorsIntersection.getHorizontalStreetSensor().setVehiclesNumber(5);
        sensorsIntersection.getVerticalStreetSensor().setVehiclesNumber(1);

        ArrayList list = new ArrayList();
        list.add(sensorsIntersection);
        timingProcessor.processTimings(list);

        assertTrue(timingProcessor.getTimings().get("S1") > timingProcessor.getTimings().get("S2"));
    }

    @Test
    public void testTotalSemaphoreTime() throws InterruptedException {
        new Thread(() -> {
            try {
                new Server( new ServerSocket(port),  new InetSocketAddress("localhost", port),"main", port).startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(3000);

        Semaphore semaphoreOne = new Semaphore(SemaphoreState.RED,
                new Pair<>(4,4),
                new Pair<>(5,5),
                StreetType.HORIZONTAL,
                "S" + 1);
        Semaphore semaphoreTwo = new Semaphore(SemaphoreState.RED,
                new Pair<>(6,6),
                new Pair<>(7,7),
                StreetType.VERTICAL,
                "S" +2);

        SemaphoresCouple semaphoresCouple = new SemaphoresCouple(semaphoreOne, semaphoreTwo, null, port);
        semaphoresCouple.start();

        Thread.sleep(3000);

        assertTrue(semaphoresCouple.getTimeMap().get("S1") + semaphoresCouple.getTimeMap().get("S2") == 40);
    }

    @Test
    public void testRemoteMode() throws InterruptedException {
        new Thread(() -> {
            try {
                new Server( new ServerSocket(port),  new InetSocketAddress("localhost", port),"main", port).startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(3000);

        Semaphore semaphoreOne = new Semaphore(SemaphoreState.RED,
                new Pair<>(4,4),
                new Pair<>(5,5),
                StreetType.HORIZONTAL,
                "S" + 1);
        Semaphore semaphoreTwo = new Semaphore(SemaphoreState.RED,
                new Pair<>(6,6),
                new Pair<>(7,7),
                StreetType.VERTICAL,
                "S" +2);

        SemaphoresCouple semaphoresCouple = new SemaphoresCouple(semaphoreOne, semaphoreTwo, null, port);
        semaphoresCouple.start();

        Thread.sleep(3000);

        assertEquals(semaphoresCouple.getMode(), "remote");

    }

    @Test
    public void testAutonomousModeAfterCrash() throws InterruptedException {
        new Thread(() -> {
            try {
                server = new Server( new ServerSocket(port),  new InetSocketAddress("localhost", portx  ),"main", port);
                server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(3000);

        Semaphore semaphoreOne = new Semaphore(SemaphoreState.RED,
                new Pair<>(4,4),
                new Pair<>(5,5),
                StreetType.HORIZONTAL,
                "S" + 1);
        Semaphore semaphoreTwo = new Semaphore(SemaphoreState.RED,
                new Pair<>(6,6),
                new Pair<>(7,7),
                StreetType.VERTICAL,
                "S" +2);

        SemaphoresCouple semaphoresCouple = new SemaphoresCouple(semaphoreOne, semaphoreTwo, null, port);
        semaphoresCouple.start();

        Thread.sleep(3000);

        assertEquals(semaphoresCouple.getMode(), "remote");

        server.crash();

        Thread.sleep(50000);

        assertEquals(semaphoresCouple.getMode(), "autonomous");
    }

}
