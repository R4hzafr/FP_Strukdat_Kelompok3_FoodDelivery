import model.*;
import tree.MinHeap;
import graph.Graph;

import java.util.*;

public class Main {

    static Scanner sc       = new Scanner(System.in);
    static MinHeap heap     = new MinHeap();
    static Graph   graph    = new Graph();
    static List<Order> orders;
    static int counter    = 1;
    static int vipCounter = 1;

    public static void main(String[] args) {

        graph.loadFromCSV("data/nodes.csv", "data/edges.csv");

        orders = CSVLoader.loadOrders("data/orders.csv");
        counter = orders.size() + 1;
        for (Order o : orders) {
            heap.enqueue(o);
        }

        while (true) {
            showMenu();
            int choice = readInt();

            switch (choice) {
                case 1: addOrder();            break;
                case 2: showOrders();          break;
                case 3: searchOrder();         break;
                case 4: deleteOrder();         break;
                case 5: processOrder();        break;
                case 6: findRoute();           break;
                case 7: graph.displayGraph();  break;
                case 8: simulationMenu();      break;
                case 0:
                    System.out.println("Keluar dari program Food Delivery.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid, masukkan angka 0-8.");
            }
        }
    }

    static int readInt() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine(); 
                System.out.print("Input harus berupa angka, coba lagi: ");
            }
        }
    }

    static void showMenu() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║             SISTEM FOOD DELIVERY                 ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║ 1. Tambah Order                                  ║");
        System.out.println("║ 2. Lihat Antrian Order                           ║");
        System.out.println("║ 3. Cari Order                                    ║");
        System.out.println("║ 4. Hapus Order                                   ║");
        System.out.println("║ 5. Proses Order                                  ║");
        System.out.println("║ 6. Cari Rute                                     ║");
        System.out.println("║ 7. Tampilkan Peta Jaringan                       ║");
        System.out.println("║ 8. Simulasi                                      ║");
        System.out.println("║ 0. Keluar                                        ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.print("Pilih menu: ");
    }
 
    static void showSimulationMenu() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                  MENU SIMULASI                   ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║ 1. Simulasi Order VIP                            ║");
        System.out.println("║ 2. Simulasi Jalan Ditutup                        ║");
        System.out.println("║ 3. Cek Keterhubungan (BFS)                       ║");
        System.out.println("║ 4. Perbandingan Strategi Pengantaran             ║");
        System.out.println("║ 0. Kembali                                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.print("Pilih simulasi: ");
    }


    
    static String pilihNode(String tipe, String label) {
        Map<String, Node> semuaNode = graph.getNodes();
        List<Node> filtered = new ArrayList<>();

        for (Node n : semuaNode.values()) {
            if (n.nodeType.equalsIgnoreCase(tipe)) {
                filtered.add(n);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("Tidak ada " + label + " yang tersedia.");
            return null;
        }

        filtered.sort(Comparator.comparing(n -> n.nodeId));

        System.out.println("\nDaftar " + label + ":");
        for (int i = 0; i < filtered.size(); i++) {
            System.out.printf("  %d. %-30s (%s)%n",
                i + 1,
                filtered.get(i).namaLokasi,
                filtered.get(i).nodeId);
        }

        int idx = -1;
        while (idx < 0 || idx >= filtered.size()) {
            System.out.print("Pilih nomor " + label + ": ");
            idx = readInt() - 1;  
            if (idx < 0 || idx >= filtered.size()) {
                System.out.println("Nomor tidak valid (1-" + filtered.size() + "), coba lagi.");
            }
        }
        return filtered.get(idx).nodeId;
    }

    static List<Node> tampilSemuaNode() {
        List<Node> semuaNode = new ArrayList<>(graph.getNodes().values());
        semuaNode.sort(Comparator.comparing(n -> n.nodeId));

        System.out.println("\nDaftar Lokasi:");
        for (int i = 0; i < semuaNode.size(); i++) {
            System.out.printf("  %2d. %-28s (%s - %s)%n",
                i + 1,
                semuaNode.get(i).namaLokasi,
                semuaNode.get(i).nodeId,
                semuaNode.get(i).nodeType);
        }
        return semuaNode;
    }

    static String pilihDariSemuaNode(String label, List<Node> semuaNode) {
        int idx = -1;
        while (idx < 0 || idx >= semuaNode.size()) {
            System.out.print("Pilih nomor " + label + ": ");
            idx = readInt() - 1;
            if (idx < 0 || idx >= semuaNode.size()) {
                System.out.println("Nomor tidak valid (1-" + semuaNode.size() + "), coba lagi.");
            }
        }
        return semuaNode.get(idx).nodeId;
    }

    static void printOrderDetail(Order o) {
        Node resto     = graph.getNode(o.restaurantNode);
        Node pelanggan = graph.getNode(o.customerNode);
        System.out.println("ID        : " + o.orderId);
        System.out.println("Restoran  : " + (resto     != null ? resto.namaLokasi     : o.restaurantNode));
        System.out.println("Tujuan    : " + (pelanggan != null ? pelanggan.namaLokasi : o.customerNode));
        System.out.println("Deadline  : " + o.deadlineMenit + " menit");
        System.out.println("Prioritas : " + o.priorityLevel);
        System.out.println("Status    : " + o.statusOrder);
        System.out.println("-".repeat(32));
    }

    static void addOrder() {
        String id = String.format("ORD%03d", counter++);
        System.out.println("\n-------- TAMBAH ORDER BARU --------");

        String r = pilihNode("Restaurant", "Restoran");
        if (r == null) return;

        String c = pilihNode("Customer", "Lokasi Pengiriman");
        if (c == null) return; 

        System.out.print("\nDeadline (menit): ");
        int d = readInt();

        if (d <= 0) {
            System.out.println("Deadline harus lebih dari 0 menit.");
            counter--; 
            return;
        }

        System.out.println("Prioritas:");
        System.out.println("  1. Normal");
        System.out.println("  2. High");
        System.out.println("  3. Urgent");
        System.out.print("Pilih prioritas: ");
        int pChoice = readInt();
        String p = pChoice == 3 ? "Urgent" : pChoice == 2 ? "High" : "Normal";

        Order o = new Order(id, r, c, d, p, "Pending");
        heap.enqueue(o);
        System.out.println("Order " + id + " berhasil ditambahkan!");
    }

    static void showOrders() {
        if (heap.isEmpty()) {
            System.out.println("Antrian order kosong.");
            return;
        }
        heap.displayAll();
    }

    static void searchOrder() {
        System.out.println("\n-------- CARI ORDER --------");
        System.out.println("Cari berdasarkan:");
        System.out.println("  1. ID Order       (contoh: ORD001)");
        System.out.println("  2. Nama Pelanggan (contoh: Budi)");
        System.out.print("Pilih: ");
        int mode = readInt();

        switch (mode) {
            case 1: searchById();            break;
            case 2: searchByNamaPelanggan(); break;
            default: System.out.println("Pilihan tidak valid.");
        }
    }

    static void searchById() {
        System.out.print("Masukkan ID Order: ");
        String id = sc.next().toUpperCase();

        if (id.isEmpty()) {
            System.out.println("ID tidak boleh kosong.");
            return;
        }

        boolean found = false;
        for (Order o : heap.getAll()) {
            if (o.orderId.equalsIgnoreCase(id)) {
                System.out.println("\n-------- ORDER DITEMUKAN --------");
                printOrderDetail(o);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Order dengan ID \"" + id + "\" tidak ditemukan.");
        }
    }

    static void searchByNamaPelanggan() {
        sc.nextLine(); 
        System.out.print("Masukkan nama pelanggan: ");
        String nama = sc.nextLine().toLowerCase().trim();

        if (nama.isEmpty()) {
            System.out.println("Nama tidak boleh kosong.");
            return;
        }

        boolean found = false;
        for (Order o : heap.getAll()) {
            Node pelanggan = graph.getNode(o.customerNode);
            if (pelanggan != null && pelanggan.namaLokasi.toLowerCase().contains(nama)) {
                System.out.println("\n-------- ORDER DITEMUKAN --------");
                printOrderDetail(o);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Tidak ada order untuk pelanggan \"" + nama + "\".");
        }
    }

    static void deleteOrder() {
        if (heap.isEmpty()) {
            System.out.println("Antrian sudah kosong, tidak ada yang bisa dihapus.");
            return;
        }

        System.out.print("\nMasukkan ID Order yang ingin dihapus: ");
        String id = sc.next().toUpperCase();

        List<Order> semua = heap.getAll();
        boolean found = false;
        for (Order o : semua) {
            if (o.orderId.equalsIgnoreCase(id)) {
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Order \"" + id + "\" tidak ditemukan.");
            return;
        }

        // Konfirmasi sebelum hapus
        System.out.print("Yakin hapus order " + id + "? (y/n): ");
        String konfirmasi = sc.next();
        if (!konfirmasi.equalsIgnoreCase("y")) {
            System.out.println("Penghapusan dibatalkan.");
            return;
        }

        System.out.println("\n[Membangun ulang antrian...]");
        heap = new MinHeap();
        for (Order o : semua) {
            if (!o.orderId.equalsIgnoreCase(id)) {
                heap.enqueue(o);
            }
        }
        System.out.println("Order " + id + " berhasil dihapus dari antrian.");
    }

    static void processOrder() {
        if (heap.isEmpty()) {
            System.out.println("Tidak ada order di dalam antrian.");
            return;
        }

        Order o = heap.dequeue();
        if (o == null) {
            System.out.println("Tidak ada order di dalam antrian.");
            return;
        }

        Node resto     = graph.getNode(o.restaurantNode);
        Node pelanggan = graph.getNode(o.customerNode);

        System.out.println("\n-------- MEMPROSES ORDER --------");
        System.out.println("ID       : " + o.orderId);
        System.out.println("Restoran : " + (resto     != null ? resto.namaLokasi     : o.restaurantNode));
        System.out.println("Tujuan   : " + (pelanggan != null ? pelanggan.namaLokasi : o.customerNode));

        Graph.ShortestPathResult path = graph.dijkstra(o.restaurantNode, o.customerNode);

        if (path == null) {
            System.out.println("[PERINGATAN] Rute tidak ditemukan!");
            System.out.println("Order " + o.orderId + " gagal dikirim, dikembalikan ke antrian.");
            heap.enqueue(o); 
            return;
        }

        path.display();
        System.out.println("Order " + o.orderId + " selesai dikirim!");
    }

    static void findRoute() {
        System.out.println("\n-------- CARI RUTE --------");
        List<Node> semuaNode = tampilSemuaNode();

        if (semuaNode.isEmpty()) {
            System.out.println("Tidak ada lokasi di dalam graph.");
            return;
        }

        int fromIdx = -1, toIdx = -1;
        while (fromIdx < 0 || fromIdx >= semuaNode.size()) {
            System.out.print("Pilih nomor lokasi asal   : ");
            fromIdx = readInt() - 1;
        }
        while (toIdx < 0 || toIdx >= semuaNode.size()) {
            System.out.print("Pilih nomor lokasi tujuan : ");
            toIdx = readInt() - 1;
        }

        if (fromIdx == toIdx) {
            System.out.println("Lokasi asal dan tujuan sama, tidak perlu rute.");
            return;
        }

        String from = semuaNode.get(fromIdx).nodeId;
        String to   = semuaNode.get(toIdx).nodeId;

        Graph.ShortestPathResult result = graph.dijkstra(from, to);

        if (result == null) {
            System.out.println("Rute dari " + semuaNode.get(fromIdx).namaLokasi
                + " ke " + semuaNode.get(toIdx).namaLokasi + " tidak ditemukan.");
            System.out.println("Coba cek apakah ada jalan yang ditutup (menu Simulasi).");
            return;
        }
        result.display();
    }

    static void simulationMenu() {
        while (true) {
            showSimulationMenu();
            int choice = readInt();

            switch (choice) {
                case 1: simulateVIP();                   break;
                case 2: simulateRoadClosure();           break;
                case 3: checkConnectivity();             break;
                case 4: simulasiPerbandinganStrategi();  break;
                case 0: return;
                default: System.out.println("Pilihan tidak valid.");
            }
        }
    }

    static void simulateVIP() {
        System.out.println("\n-------- SIMULASI ORDER VIP --------");
        String vipId = String.format("VIP-%03d", vipCounter++);

        String r = pilihNode("Restaurant", "Restoran");
        if (r == null) return;

        String c = pilihNode("Customer", "Lokasi Pengiriman");
        if (c == null) return;

        Order vip = new Order(vipId, r, c, 5, "Urgent", "Pending");
        heap.insertVIPOrder(vip);
    }

    static void simulateRoadClosure() {
        System.out.println("\n-------- SIMULASI PENUTUPAN JALAN --------");
        List<Node> semuaNode = tampilSemuaNode();
        if (semuaNode.isEmpty()) return;

        int aIdx = -1, bIdx = -1;
        while (aIdx < 0 || aIdx >= semuaNode.size()) {
            System.out.print("Pilih nomor lokasi pertama: ");
            aIdx = readInt() - 1;
        }
        while (bIdx < 0 || bIdx >= semuaNode.size()) {
            System.out.print("Pilih nomor lokasi kedua  : ");
            bIdx = readInt() - 1;
        }

        if (aIdx == bIdx) {
            System.out.println("Lokasi pertama dan kedua sama, tidak ada jalan yang ditutup.");
            return;
        }

        String a     = semuaNode.get(aIdx).nodeId;
        String b     = semuaNode.get(bIdx).nodeId;
        String namaA = semuaNode.get(aIdx).namaLokasi;
        String namaB = semuaNode.get(bIdx).namaLokasi;

        graph.closeEdge(a, b);
        System.out.println("Jalan " + namaA + " <-> " + namaB + " berhasil ditutup.");
        System.out.println("Rute yang melewati jalan ini tidak akan tersedia.");
    }

    static void checkConnectivity() {
        System.out.println("\n-------- CEK KETERHUBUNGAN (BFS) --------");
        List<Node> semuaNode = tampilSemuaNode();
        if (semuaNode.isEmpty()) return;

        int aIdx = -1, bIdx = -1;
        while (aIdx < 0 || aIdx >= semuaNode.size()) {
            System.out.print("Pilih nomor lokasi asal   : ");
            aIdx = readInt() - 1;
        }
        while (bIdx < 0 || bIdx >= semuaNode.size()) {
            System.out.print("Pilih nomor lokasi tujuan : ");
            bIdx = readInt() - 1;
        }

        String a     = semuaNode.get(aIdx).nodeId;
        String b     = semuaNode.get(bIdx).nodeId;
        String namaA = semuaNode.get(aIdx).namaLokasi;
        String namaB = semuaNode.get(bIdx).namaLokasi;

        if (aIdx == bIdx) {
            System.out.println(namaA + " -> " + namaB);
            System.out.println("Status: [Terhubung] (lokasi yang sama)");
            return;
        }

        boolean result = graph.isConnected(a, b);
        System.out.println("\n" + namaA + " -> " + namaB);
        System.out.println("Status: " + (result ? "[Terhubung]" : "[Tidak Terhubung]"));
    }

    static void simulasiPerbandinganStrategi() {
        System.out.println("\n--- PERBANDINGAN STRATEGI PENGANTARAN ---");

        List<Order> semua = heap.getAll();

        if (semua.isEmpty()) {
            System.out.println("Antrian kosong, tidak ada yang dibandingkan.");
            return;
        }

        List<Order> byPrioritas = new ArrayList<>(semua);
        Collections.sort(byPrioritas);

        List<Order> byDeadline = new ArrayList<>(semua);
        byDeadline.sort(Comparator.comparingInt(o -> o.deadlineMenit));

        List<Order> byJarak = new ArrayList<>(semua);
        byJarak.sort((a, b) -> {
            Graph.ShortestPathResult ra = graph.dijkstra(a.restaurantNode, a.customerNode);
            Graph.ShortestPathResult rb = graph.dijkstra(b.restaurantNode, b.customerNode);
            double jarakA = (ra != null) ? ra.totalJarak : Double.MAX_VALUE;
            double jarakB = (rb != null) ? rb.totalJarak : Double.MAX_VALUE;
            return Double.compare(jarakA, jarakB);
        });

        System.out.println("\nStrategi 1 - Prioritas + Deadline (MinHeap / default sistem):");
        System.out.println("-".repeat(50));
        for (int i = 0; i < byPrioritas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + byPrioritas.get(i));
        }

        System.out.println("\nStrategi 2 - Deadline Tercepat (tanpa lihat level prioritas):");
        System.out.println("-".repeat(50));
        for (int i = 0; i < byDeadline.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + byDeadline.get(i));
        }

        System.out.println("\nStrategi 3 - Jarak Terdekat (paling efisien untuk driver):");
        System.out.println("-".repeat(50));
        for (int i = 0; i < byJarak.size(); i++) {
            Order o = byJarak.get(i);
            Graph.ShortestPathResult r = graph.dijkstra(o.restaurantNode, o.customerNode);
            double jarak = (r != null) ? r.totalJarak : 0.0;
            System.out.printf("  %d. %s  [%.2f km]%n", i + 1, o, jarak);
        }
    }
}