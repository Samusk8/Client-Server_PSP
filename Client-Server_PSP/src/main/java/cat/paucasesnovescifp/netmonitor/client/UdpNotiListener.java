package cat.paucasesnovescifp.netmonitor.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpNotiListener implements Runnable {
    private final DatagramSocket socket;

    public UdpNotiListener(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            while (!socket.isClosed()) {
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("[UDP] " + msg);
            }
        } catch (IOException e) {
            System.out.println("UDP listener cerrado.");
        }
    }
}
