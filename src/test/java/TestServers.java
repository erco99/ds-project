import fe.app.model.tfmanagement.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;


public class TestServers {

    private static Server first_server;
    private static Server second_server;
    private static int PORT = 2001;

    @BeforeEach
    public void beforeEach() {
        PORT++;
    }

    @Test
    public void testBackupBecomesMain() throws InterruptedException {
        new Thread(() -> {
            try {
                first_server = new Server( new ServerSocket(PORT),  new InetSocketAddress("localhost", PORT),"MAIN", PORT);
                first_server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                second_server = new Server( null,  new InetSocketAddress("localhost", PORT),"BACKUP", PORT);
                second_server.startServerAsBackup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(2000);

        assertEquals(first_server.getServerType(), "MAIN");
        assertEquals(second_server.getServerType(), "BACKUP");

        first_server.crash();

        Thread.sleep(2000);

        assertEquals(second_server.getServerType(), "MAIN");

    }

    @Test
    public void testMainBecomesBackupAfterRestart() throws InterruptedException {
        new Thread(() -> {
            try {
                first_server = new Server( new ServerSocket(PORT),  new InetSocketAddress("localhost", PORT),"MAIN", PORT);
                first_server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                second_server = new Server( null,  new InetSocketAddress("localhost", PORT),"BACKUP", PORT);
                second_server.startServerAsBackup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(2000);

        assertEquals(first_server.getServerType(), "MAIN");
        assertEquals(second_server.getServerType(), "BACKUP");

        first_server.crash();

        Thread.sleep(2000);

        assertEquals(second_server.getServerType(), "MAIN");

        first_server.restart();
        Thread.sleep(3000);

        assertEquals(first_server.getServerType(), "BACKUP");

    }

    @Test
    public void testBackupCrashWhenMainOn() throws InterruptedException {
        new Thread(() -> {
            try {
                first_server = new Server( new ServerSocket(PORT),  new InetSocketAddress("localhost", PORT),"MAIN", PORT);
                first_server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                second_server = new Server( null,  new InetSocketAddress("localhost", PORT),"BACKUP", PORT);
                second_server.startServerAsBackup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(2000);

        second_server.crash();

        Thread.sleep(1000);

        second_server.restart();

        Thread.sleep(1000);

        assertEquals(second_server.getServerType(), "BACKUP");
    }

    @Test
    public void testMainCrashWhenBackupDown() throws InterruptedException {
        new Thread(() -> {
            try {
                first_server = new Server( new ServerSocket(PORT),  new InetSocketAddress("localhost", PORT),"MAIN", PORT);
                first_server.startServerAsMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                second_server = new Server( null,  new InetSocketAddress("localhost", PORT),"BACKUP", PORT);
                second_server.startServerAsBackup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(2000);

        second_server.crash();
        Thread.sleep(1000);

        first_server.crash();
        Thread.sleep(1000);

        first_server.restart();
        Thread.sleep(1000);

        assertEquals(first_server.getServerType(), "MAIN");
    }
}
