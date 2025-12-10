package cat.paucasesnovescifp.netmonitor.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    public static int port = 5000;
    public static List<ClientHandler> handlers = new ArrayList<>();
    public static List<ClientInfo> clientes = new ArrayList<>();

    static void main() {
        try (ServerSocket server = new ServerSocket(port)){
            while (true) {
                Socket client = server.accept();

                ClientHandler ch = new ClientHandler(client);
                handlers.add(ch);

                Thread hilo = new Thread(ch);
                hilo.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void addClient(ClientInfo c){
        synchronized (clientes){
            boolean existe = false;
            for (ClientInfo cc : clientes) {
                if (cc.getAddress().equals(c.getAddress())) {
                    existe = true;
                    System.out.println("El cliente ya existe");
                    break;
                }
                if (!existe) {
                    clientes.add(c);
                    System.out.println("Cliente "+c+" añadido");
                }
            }
        }
    }

    public static void removeClient(ClientInfo c){}
}
