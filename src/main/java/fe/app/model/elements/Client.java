package fe.app.model.elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    private Socket socket;
    public static final int PORT = 2000;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", PORT);
        Client client = new Client(socket);
        client.listen();
    }

    private void listen() throws IOException {
        byte[] buffer = new byte[1024];

        try {
            InputStream inputStream = socket.getInputStream ();
            while (true) {
                int readBytes = inputStream.read(buffer);
                System.out.println(readBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("eccomi" + socket.getInputStream().read());
    }


}
