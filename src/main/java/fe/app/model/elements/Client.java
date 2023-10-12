package fe.app.model.elements;

import java.io.IOException;
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

    private void listen() {
        System.out.println("eccomi");
    }


}
