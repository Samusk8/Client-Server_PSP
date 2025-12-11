package cat.paucasesnovescifp.netmonitor.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    public static int port = 5000;
    public static int udpPort = 5001;
    public static List<ClientHandler> clientesConectados  = new ArrayList<>();
    public static List<ClientInfo> clientes = new ArrayList<>();
    public static UdpBroadcaster broadcaster;

    static void main() {
        try{
            broadcaster = new UdpBroadcaster(udpPort);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            return;
        }
        try (ServerSocket server = new ServerSocket(port)){
            while (true) {
                Socket client = server.accept();

                ClientHandler ch = new ClientHandler(client);
                clientesConectados.add(ch);

                Thread hilo = new Thread(ch);
                hilo.start();

                broadcaster.broadcast("Se ha conectado el cliente: "+ client.getInetAddress(),getClients());
                broadcaster.broadcast("Hay "+ clientesConectados.size()+" clientes conectados",getClients());
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

    public static void removeClient(InetAddress address, int udpPort){
        synchronized (clientes){
            clientes.removeIf(c -> c.getAddress().equals(address) && c.getUdpPort() == udpPort);
        }
    }

    public static List<ClientInfo> getClients() {
        synchronized (clientes){
            return List.copyOf(clientes);
        }
    }
}
