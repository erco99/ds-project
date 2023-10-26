package fe.app.model.tfmanagement.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 2000;
    private ServerSocket serverSocket;
    private Socket socket;
    private TimingProcessor timingProcessor = new TimingProcessor();

    public Server(ServerSocket serverSocket, Socket socket) {
        this.serverSocket = serverSocket;
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        boolean isMainServer;
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            isMainServer = true;
        } catch (Exception e) {
            socket = new Socket("localhost", PORT);
            isMainServer = false;
        }

        if (isMainServer) {
            Server server = new Server(serverSocket, null);
            server.startServerAsMain();
        } else {
            Server server = new Server(null, socket);
            server.startServerAsBackup();
        }

    }

    public void startServerAsMain() throws IOException {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ServerHandler serverHandler = new ServerHandler(socket, timingProcessor);
                serverHandler.start();
                System.out.printf("Accepted connection from: %s, on local port %d\n",
                        socket.getRemoteSocketAddress(), PORT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServerAsBackup() throws IOException {
        System.out.println("eccomi" + socket.getInputStream().read());
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
}
