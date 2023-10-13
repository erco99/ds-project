package fe.app.model.elements;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 2000;
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);
        server.startServer();
    }

    public void startServer() throws IOException {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                System.out.printf("Accepted connection from: %s, on local port %d\n",
                        socket.getRemoteSocketAddress(), PORT);
                System.out.println("dskdsdsds");
                var buffer = new byte[1024];
                int readBytes = System.in.read(buffer);

                socket.getOutputStream().write(buffer,0,readBytes);
                socket.getOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
