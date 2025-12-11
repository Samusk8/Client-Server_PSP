package cat.paucasesnovescifp;

import cat.paucasesnovescifp.netmonitor.client.TcpClient;

import java.util.ArrayList;
import java.util.List;
public class Main {

        public static void main(String[] args) {
            int numClients = 10; // cantidad de clientes que quieres abrir
            List<Thread> threads = new ArrayList<>();

            for (int i = 0; i < numClients; i++) {
                Thread t = new Thread(() -> TcpClient.main(new String[0]));
                t.start();
                threads.add(t);
                try {
                    Thread.sleep(200); // separa ligeramente la conexión para no saturar
                } catch (InterruptedException ignored) {}
            }

            // Opcional: esperar a que todos terminen
            for (Thread t : threads) {
                try { t.join(); } catch (InterruptedException ignored) {}
            }
        }


}