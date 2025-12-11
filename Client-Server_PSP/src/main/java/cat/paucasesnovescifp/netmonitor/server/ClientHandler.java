package cat.paucasesnovescifp.netmonitor.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable{
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private int udpPort;

    public ClientHandler(Socket client) {
        this.client = client;
    }


    @Override
    public void run() {
        System.out.println("Client handler inisiau");
        try {
            out = new PrintWriter(client.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            sendMessage("Cliente "+ client+" Se ha conectado");

            String line;

            while ((line = in.readLine()) != null) {
                line = line.trim();
                if(line.equalsIgnoreCase("time")){
                    String hora = LocalDateTime.now().toString();
                    sendMessage("Hora: "+hora);
                } else if (line.toLowerCase().startsWith("echo")) {
                    //String echo = in.readLine();
                    String text = line.substring(5);
                    sendMessage("Mensaje: "+text);
                } else if (line.equalsIgnoreCase("count")) {
                    sendMessage("Count: "+TCPServer.clientesConectados.size());
                } else if (line.equalsIgnoreCase("shutdown")) {
                    if(client.getInetAddress().toString().equalsIgnoreCase("127.0.0.1")){
                        sendMessage("apagando servidor...");
                        System.exit(0);
                    } else {
                        sendMessage("Shutdown solo desde localhost");
                    }
                } else if (line.toLowerCase().startsWith("udpport")) {
                    try {
                        int port = Integer.parseInt(line.substring(8).trim());
                        this.udpPort = port;

                        ClientInfo cliente = new ClientInfo(client.getInetAddress(),port);
                        TCPServer.addClient(cliente);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                } else {
                    sendMessage("Comando desconocido");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    private void broadcast(String message){
        for (ClientHandler ch : TCPServer.clientesConectados) {
            ch.sendMessage(message);
        }
    }

    private void disconnect() throws IOException {
        try {
            if (client != null && !client.isClosed()) client.close();
        } catch (IOException e) {}

        TCPServer.clientesConectados.remove(this);
        TCPServer.removeClient(client.getInetAddress(), udpPort);
        System.out.println("[TCP] Cliente desconectado: "+client.getInetAddress().getHostAddress() + client.getPort());
        UdpBroadcaster broadcaster = TCPServer.broadcaster;
        if (broadcaster!=null){
            broadcaster.broadcast("Se ha conectado el cliente: "+ client.getInetAddress(),TCPServer.getClients());
            broadcaster.broadcast("Hay "+ TCPServer.clientesConectados.size()+" clientes conectados",TCPServer.getClients());
        }

    }
}
