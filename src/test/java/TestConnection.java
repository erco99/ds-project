import fe.app.controller.NetworkController;
import fe.app.controller.SensorsController;
import fe.app.model.elements.map.MapContext;
import fe.app.model.elements.map.StreetMap;
import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.model.tfmanagement.semaphore.SemaphoreState;
import fe.app.model.tfmanagement.semaphore.SemaphoresCouple;
import fe.app.model.tfmanagement.server.Server;
import fe.app.util.Pair;
import fe.app.util.StreetType;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class TestConnection {

    private static Server server;
    public static int port = 2020;
    private static StreetMap streetMap;
    private static MapContext mapContext;
    private static NetworkController networkController;
    private static SensorsController sensorsController;

    @BeforeAll
    public static void beforeAll() {
        streetMap = new StreetMap(null);
    }

    @BeforeEach
    public void beforeEach() {
        port++;
    }

    @Test
    public void testSemaphoreCoupleConnectionWithoutServer() {
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

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<?> future = es.submit(() -> {
            semaphoresCouple.run();
            return null;
        });

        assertThrows(ExecutionException.class, () -> future.get());
    }

    @Test
    public void testServerStatusWhenServerDown() {
        mapContext = new MapContext(streetMap);
        sensorsController = new SensorsController(mapContext);
        networkController = new NetworkController(null, sensorsController, port);
        streetMap.setSensorsController(sensorsController);

        sensorsController.start();

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<?> future = es.submit(() -> {
            networkController.run();
            return null;
        });

        //GUI is not running so it throws an exception
        assertThrows(ExecutionException.class, () -> future.get());

        assertFalse(networkController.isServerUp());
    }

    @Test
    public void testSemaphoresWhenServerUp() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                server = new Server( new ServerSocket(port),  new InetSocketAddress("localhost", port),"main", port);
                server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
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

        try {
            SemaphoresCouple semaphoresCouple = new SemaphoresCouple(semaphoreOne, semaphoreTwo, null, port);

            System.out.println(semaphoresCouple.getTimeMap());
            semaphoresCouple.start();

            Thread.sleep(3000);
            System.out.println(semaphoresCouple.getTimeMap());

            server.closeServerSocket();
            thread.interrupt();
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void testServerStatusWhenServerUp() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                server = new Server( new ServerSocket(port),  new InetSocketAddress("localhost", port),"main", port);
                server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        Thread.sleep(3000);

        mapContext = new MapContext(streetMap);
        sensorsController = new SensorsController(mapContext);
        networkController = new NetworkController(null, sensorsController, port);
        streetMap.setSensorsController(sensorsController);

        sensorsController.start();

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<?> future = es.submit(() -> {
            networkController.run();
            return null;
        });

        //GUI is not running so it throws an exception
        assertThrows(ExecutionException.class, () -> future.get());
        assertTrue(networkController.isServerUp());

        server.closeServerSocket();
        thread.interrupt();
    }
}
