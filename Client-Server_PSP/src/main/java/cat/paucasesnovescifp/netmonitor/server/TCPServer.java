package cat.paucasesnovescifp.netmonitor.server;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static int port = 5000;

    static void main() {
        try (ServerSocket server = new ServerSocket(port)){
            while (true) {
                Socket client = server.accept();

                ClientHandler ch = new ClientHandler(client);
                new Thread(ch).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
