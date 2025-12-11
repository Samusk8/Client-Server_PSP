package cat.paucasesnovescifp.netmonitor.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (
                Socket tcpSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()))
        ) {
            System.out.println("Conectado al servidor tcp " + host + ":" + port);

            DatagramSocket udpSocket = new DatagramSocket();
            int udpPort = udpSocket.getLocalPort();
            System.out.println("Puerto udp: " + udpPort);
            Thread notificaciones = new Thread(new UdpNotiListener(udpSocket));
            notificaciones.start();

            out.println("UDPPORT " + udpPort);
            String response = in.readLine();
            System.out.println("[TCP] " + response);
            Scanner sc = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("-------MENU-----");
                System.out.println("1.- TIME");
                System.out.println("2.- ECHO");
                System.out.println("3.- COUNT");
                System.out.println("4.- BYE");
                System.out.println("5.- SHUTDOWN");
                System.out.print("> ");
                String line = sc.nextLine().toLowerCase().trim();

                String resp = in.readLine();
                if (resp != null) {
                    System.out.println("[TCP Server] " + resp);
                }
                if (line.equalsIgnoreCase("BYE") || line.equalsIgnoreCase("SHUTDOWN")) {
                    running = false;
                }
            }


            udpSocket.close();
            System.out.println("Cliente cerrado.");
        } catch (IOException e) {
            System.out.println("Error cliente: " + e.getMessage());
        }
    }
}
