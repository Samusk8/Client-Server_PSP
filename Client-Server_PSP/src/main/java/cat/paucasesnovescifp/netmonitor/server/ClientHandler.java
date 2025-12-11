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
                if(line.equals("time")){
                    String hora = LocalDateTime.now().toString();
                    sendMessage("Hora: "+hora);
                } else if (line.equals("echo")) {
                    //String echo = in.readLine();
                    String text = line.substring(5);
                    sendMessage("Mensaje: "+text);
                } else if (line.equals("count")) {
                    sendMessage("Count: "+TCPServer.clientesConectados.size());
                } else if (line.equals("shutdown")) {
                    if(client.getInetAddress().toString().equals("127.0.0.1")){
                        sendMessage("apagando servidor...");
                        System.exit(0);
                    } else {
                        sendMessage("Shutdown solo desde localhost");
                    }
                } else if (line.equals("udpport")) {
                    try {
                        int port = Integer.parseInt(line.substring(8).trim());

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
}
