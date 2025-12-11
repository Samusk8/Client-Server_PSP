package cat.paucasesnovescifp.netmonitor.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class UdpBroadcaster {
    private DatagramSocket udpSocket;

    public UdpBroadcaster(int puerto) throws SocketException {
        this.udpSocket = new DatagramSocket(puerto);
    }

    public void broadcast(String msg, List<ClientInfo> clientes) throws IOException {
        if (clientes.isEmpty()) {
            return;
        };

        byte[] buffer = msg.getBytes();
        for (ClientInfo client : clientes) {
            InetAddress address = client.getAddress();
            int puerto = client.getUdpPort();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, puerto);
            udpSocket.send(packet);
        }

    }
}
