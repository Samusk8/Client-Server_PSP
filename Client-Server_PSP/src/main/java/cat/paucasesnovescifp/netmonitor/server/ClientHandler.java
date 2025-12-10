package cat.paucasesnovescifp.netmonitor.server;

import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }


    @Override
    public void run() {
        System.out.println("Client handler inisiau");
    }
}
