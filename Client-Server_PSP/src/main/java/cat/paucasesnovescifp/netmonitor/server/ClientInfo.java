package cat.paucasesnovescifp.netmonitor.server;

import java.net.InetAddress;

public class ClientInfo {
    InetAddress address;
    int udpPort;

    public ClientInfo(InetAddress address,  int udpPort) {
        this.address = address;
        this.udpPort = udpPort;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "address=" + address +
                ", udpPort=" + udpPort +
                '}';
    }
}
