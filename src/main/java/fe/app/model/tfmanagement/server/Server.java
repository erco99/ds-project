package fe.app.model.tfmanagement.server;

import fe.app.model.tfmanagement.client.ClientHandler;
import fe.app.model.tfmanagement.presentation.EmptyResponse;
import fe.app.model.tfmanagement.presentation.ServerStatusRequest;
import fe.app.util.GsonUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

    public int port;
    private ServerSocket serverSocket;
    private final InetSocketAddress socket;
    private final TimingProcessor timingProcessor = new TimingProcessor();
    private final ServerView serverView;
    private String serverType;
    private boolean crashed = false;

    public Server(ServerSocket serverSocket, InetSocketAddress socket, String serverType, int port) {
        this.port = port;
        this.serverSocket = serverSocket;
        this.socket = socket;

        this.serverType = serverType;
        serverView = new ServerView(this);
        serverView.start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress socket = new InetSocketAddress("localhost", 2000);
        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            Server server = new Server(serverSocket, socket, "MAIN", 2000);
            server.startServerAsMain();
        } catch (Exception e) {
            Server server = new Server(null, socket, "BACKUP", 2000);
            server.startServerAsBackup();
        }

    }

    public void startServerAsMain() throws IOException, InterruptedException {
        serverView.setServerType("MAIN");
        serverType = "MAIN";
        if (serverSocket == null) {
            serverSocket = new ServerSocket(port);
        }
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ServerHandler serverHandler = new ServerHandler(socket, timingProcessor);
                serverHandler.start();
                System.out.printf("MAIN server: Accepted connection from: %s, on local port %d\n",
                        socket.getRemoteSocketAddress(), port);
            }
        } catch (SocketException e) {
            System.out.println("MAIN server: crashed");
            crashState();
        }
    }

    public void startServerAsBackup() throws IOException, InterruptedException {
        serverView.setServerType("BACKUP");
        serverType = "BACKUP";
        ClientHandler backupServerHandler = new ClientHandler(socket, GsonUtils.createGson());
        while (!crashed) {
            try {
                System.out.println("BACKUP server: is MAIN server up?");
                backupServerHandler.rpc(new ServerStatusRequest(), EmptyResponse.class);
                System.out.println("BACKUP server: MAIN server is up");
            } catch (Exception e) {
                System.out.println("BACKUP server: MAIN server is down");
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (crashed) {
            crashState();
        } else {
            serverSocket = new ServerSocket(port);
            startServerAsMain();
        }
    }

    private void crashState() throws InterruptedException, IOException {
        System.out.println("in crash state");
        serverSocket = null;

        while (crashed) {
            Thread.sleep(1000);
        }

        try {
            serverSocket = new ServerSocket(port);
            startServerAsMain();
        } catch (SocketException e) {
            startServerAsBackup();
        }

    }

    public void crash () {
        closeServerSocket();
        crashed = true;
    }

    public void restart() {
        this.crashed = false;
    }

    public void closeServerSocket() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerType() {
        return serverType;
    }

}
