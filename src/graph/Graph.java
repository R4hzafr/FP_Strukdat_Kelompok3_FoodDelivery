package graph;

import model.Node;
import model.Edge;
import model.CSVLoader;

import java.util.*;

public class Graph {

    // setiap node nyimpen daftar tetangganya
    private Map<String, List<Edge>> adjacencyList;

    // semua node yang ada di graph
    private Map<String, Node> nodes;

    // kumpulan edge yang lagi ditutup
    private Set<String> closedEdges;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.nodes         = new HashMap<>();
        this.closedEdges   = new HashSet<>();
    }

    // ── load data

    public void loadFromCSV(String nodesPath, String edgesPath) {
        // baca nodes.csv, masukin ke map
        nodes = CSVLoader.loadNodes(nodesPath);
        for (String id : nodes.keySet()) {
            adjacencyList.putIfAbsent(id, new ArrayList<>());
        }

        // baca edges.csv, tiap edge dijadiin dua arah
        Map<String, List<Edge>> rawEdges = CSVLoader.loadEdges(edgesPath);
        for (Map.Entry<String, List<Edge>> entry : rawEdges.entrySet()) {
            String src = entry.getKey();
            for (Edge e : entry.getValue()) {
                addEdge(src, e); // arah asli: src → dst
                addEdge(e.destination, // balik arah: dst → src
                        new Edge(src, e.waktuTempuh, e.jarak,
                                 e.statusJalan, e.biaya, e.ratingJalan));
            }
        }

        System.out.println("Graph built: " + nodes.size()
                + " nodes, " + countEdges() + " directed edges");
    }

    // ── manajemen node & edge 

    // tambah node baru ke graph (dipanggil kalau ada input node baru dari menu)
    public void addNode(Node node) {
        nodes.put(node.nodeId, node);
        adjacencyList.putIfAbsent(node.nodeId, new ArrayList<>());
    }

    // tambah satu edge satu arah
    public void addEdge(String source, Edge edge) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.get(source).add(edge);
    }

    // tambah edge dua arah sekaligus 
    public void addUndirectedEdge(String src, String dst,
                                  int waktu, double jarak,
                                  String status, int biaya, double rating) {
        addEdge(src, new Edge(dst, waktu, jarak, status, biaya, rating));
        addEdge(dst, new Edge(src, waktu, jarak, status, biaya, rating));
    }

    // tutup jalan > dijkstra & bfs bakal skip edge ini
    public void closeEdge(String source, String destination) {
        closedEdges.add(edgeKey(source, destination));
        closedEdges.add(edgeKey(destination, source));
        System.out.println("- Jalan ditutup: " + source + " <-> " + destination);
    }

    // buka jalan kembali
    public void openEdge(String source, String destination) {
        closedEdges.remove(edgeKey(source, destination));
        closedEdges.remove(edgeKey(destination, source));
        System.out.println("+ Jalan dibuka: " + source + " <-> " + destination);
    }

    public void closeMacetEdges() {
        for (Map.Entry<String, List<Edge>> entry : adjacencyList.entrySet()) {
            String src = entry.getKey();
            for (Edge e : entry.getValue()) {
                if (!e.statusJalan.equalsIgnoreCase("Perjalanan Lancar")) {
                    closeEdge(src, e.destination);
                }
            }
        }
    }

    // cek apakah edge masih aktif
    private boolean isEdgeActive(String src, String dst) {
        return !closedEdges.contains(edgeKey(src, dst));
    }

    // format key untuk closedEdges set
    private String edgeKey(String src, String dst) {
        return src + "_" + dst;
    }

    // ── dijkstra 
    // kompleksitas: O((V+E) log V) → pakai priority queue
    // bobot utama: waktuTempuh (menit)

    public ShortestPathResult dijkstra(String startId, String endId) {
        // validasi dulu node-nya ada atau tidak
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) {
            System.out.println("[X] Node tidak ditemukan: " + startId + " atau " + endId);
            return null;
        }

        // dist menyimpan waktu tercepat yang sudah ditemukan ke setiap node
        Map<String, Integer> dist = new HashMap<>();
        // prev menyimpan "dari mana kita datang" → buat rekonstruksi path nanti
        Map<String, String>  prev = new HashMap<>();

        // inisialisasi semua jarak ke tak terhingga
        for (String id : nodes.keySet()) dist.put(id, Integer.MAX_VALUE);
        // jarak ke titik awal = 0
        dist.put(startId, 0);

        // priority queue: selalu proses node dengan waktu terkecil duluan
        PriorityQueue<String> queue = new PriorityQueue<>(
                Comparator.comparingInt(id -> dist.getOrDefault(id, Integer.MAX_VALUE)));
        queue.offer(startId);

        // visited: node yang sudah diproses tidak perlu diproses lagi
        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // skip kalau sudah pernah diproses
            if (visited.contains(current)) continue;
            visited.add(current);

            // kalau sudah sampai tujuan, berhenti
            if (current.equals(endId)) break;

            // cek semua tetangga dari node sekarang
            for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                // skip jalan yang ditutup
                if (!isEdgeActive(current, edge.destination)) continue;

                // hitung waktu baru kalau lewat edge ini
                int newDist = dist.get(current) + edge.waktuTempuh;

                // kalau lebih cepat dari yang sudah ada, update
                if (newDist < dist.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
                    dist.put(edge.destination, newDist);
                    prev.put(edge.destination, current); // catat kita datang dari mana
                    queue.offer(edge.destination);
                }
            }
        }

        // kalau masih MAX_VALUE berarti tidak ada jalur sama sekali
        if (dist.get(endId) == Integer.MAX_VALUE) {
            System.out.println("! Tidak ada jalur dari " + startId + " ke " + endId);
            return null;
        }

        // rekonstruksi path: mundur dari endId sampai startId pakai prev
        List<String> path = new ArrayList<>();
        String cur = endId;
        while (cur != null) {
            path.add(0, cur); // tambah ke depan supaya urutannya benar
            cur = prev.get(cur);
        }

        // hitung total jarak dan biaya sepanjang path
        double totalJarak = 0;
        int    totalBiaya = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Edge e = getEdge(path.get(i), path.get(i + 1));
            if (e != null) {
                totalJarak += e.jarak;
                totalBiaya += e.biaya;
            }
        }

        return new ShortestPathResult(path, dist.get(endId), totalJarak, totalBiaya, nodes);
    }

    // ── bfs 
    // kompleksitas: O(V+E) → kunjungi semua vertex dan edge sekali
    // dipakai untuk validasi keterhubungan area

    // cek apakah dua node masih terhubung (return true/false)
    public boolean isConnected(String startId, String endId) {
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) {
            System.out.println("! Node tidak ditemukan.");
            return false;
        }

        Set<String>   visited = new HashSet<>();
        Queue<String> queue   = new LinkedList<>();

        // mulai dari startId
        queue.offer(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            // ketemu tujuan → terhubung
            if (current.equals(endId)) return true;

            // tambah semua tetangga yang belum dikunjungi dan jalannya aktif
            for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(edge.destination)
                        && isEdgeActive(current, edge.destination)) {
                    visited.add(edge.destination);
                    queue.offer(edge.destination);
                }
            }
        }
        // habis semua node tapi tidak ketemu → tidak terhubung
        return false;
    }

    // return semua node yang bisa dicapai dari startId
    public Set<String> bfsReachable(String startId) {
        Set<String>   visited = new LinkedHashSet<>();
        Queue<String> queue   = new LinkedList<>();

        queue.offer(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(edge.destination)
                        && isEdgeActive(current, edge.destination)) {
                    visited.add(edge.destination);
                    queue.offer(edge.destination);
                }
            }
        }
        return visited;
    }

    // bfs level by level > bagus buat visualisasi
    public void bfsLevelTraversal(String startId) {
        if (!nodes.containsKey(startId)) {
            System.out.println("! Node tidak ditemukan: " + startId);
            return;
        }

        System.out.println("\n--- BFS Level Traversal dari: "
                + startId + " (" + nodes.get(startId).namaLokasi + ")");
        System.out.println("─".repeat(50));

        Set<String>          visited = new LinkedHashSet<>();
        Queue<String>        queue   = new LinkedList<>();
        Map<String, Integer> level   = new HashMap<>();

        queue.offer(startId);
        visited.add(startId);
        level.put(startId, 0);

        while (!queue.isEmpty()) {
            String current      = queue.poll();
            int    currentLevel = level.get(current);
            Node   n            = nodes.get(current);
            String label        = (n != null) ? n.namaLokasi : current;

            // indent sesuai level untuk visualisasi hierarki
            System.out.println("  ".repeat(currentLevel) + "Level " + currentLevel
                    + " → " + current + " (" + label + ")");

            for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(edge.destination)
                        && isEdgeActive(current, edge.destination)) {
                    visited.add(edge.destination);
                    level.put(edge.destination, currentLevel + 1);
                    queue.offer(edge.destination);
                }
            }
        }

        int total        = visited.size();
        int disconnected = nodes.size() - total;
        System.out.println("─".repeat(50));
        System.out.println("Total node terjangkau: " + total + " / " + nodes.size());
        if (disconnected > 0) {
            System.out.println("!!!  " + disconnected + " node TIDAK terhubung dari " + startId);
        }
    }

    // ── display

    public void displayGraph() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║           ADJACENCY LIST — FOOD DELIVERY          ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // urutkan node supaya tampilan konsisten
        List<String> sortedIds = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sortedIds);

        for (String nodeId : sortedIds) {
            Node   n     = nodes.get(nodeId);
            String label = (n != null) ? n.namaLokasi : nodeId;
            System.out.printf("%-4s %-28s -> ", nodeId, "(" + label + ")");

            List<Edge> edges = adjacencyList.get(nodeId);
            if (edges == null || edges.isEmpty()) {
                System.out.print("[no edges]");
            } else {
                StringJoiner sj = new StringJoiner(", ");
                for (Edge e : edges) {
                    // kasih tanda - kalau jalan sedang ditutup
                    String mark = !isEdgeActive(nodeId, e.destination) ? "-" : "";
                    sj.add(mark + e.destination + "(" + e.waktuTempuh + "m)");
                }
                System.out.print(sj);
            }
            System.out.println();
        }
        System.out.println();
    }

    // ── getter 

    public Map<String, Node>        getNodes()          { return nodes; }
    public Map<String, List<Edge>>  getAdjacencyList()  { return adjacencyList; }
    public Node                     getNode(String id)  { return nodes.get(id); }
    public List<Edge>               getNeighbors(String id) {
        return adjacencyList.getOrDefault(id, Collections.emptyList());
    }

    // cari edge spesifik dari src ke dst (hanya yang aktif)
    public Edge getEdge(String src, String dst) {
        for (Edge e : adjacencyList.getOrDefault(src, Collections.emptyList())) {
            if (e.destination.equals(dst) && isEdgeActive(src, dst)) return e;
        }
        return null;
    }

    public int              getTotalNodes()     { return nodes.size(); }
    public int              countEdges()        { return adjacencyList.values().stream().mapToInt(List::size).sum(); }
    public Set<String>      getClosedEdges()    { return Collections.unmodifiableSet(closedEdges); }

    // ── inner class: hasil dijkstra 
    public static class ShortestPathResult {
        public final List<String>       path;
        public final int                totalWaktu; // menit
        public final double             totalJarak; // km
        public final int                totalBiaya; // rupiah
        private final Map<String, Node> nodes;

        public ShortestPathResult(List<String> path, int totalWaktu,
                                  double totalJarak, int totalBiaya,
                                  Map<String, Node> nodes) {
            this.path       = path;
            this.totalWaktu = totalWaktu;
            this.totalJarak = totalJarak;
            this.totalBiaya = totalBiaya;
            this.nodes      = nodes;
        }

        public void display() {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║              HASIL RUTE DIJKSTRA                 ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.println("Rute: ");
            for (int i = 0; i < path.size(); i++) {
                String id    = path.get(i);
                Node   n     = nodes.get(id);
                String label = (n != null) ? n.namaLokasi : id;
                System.out.print("  " + id + " (" + label + ")");
                if (i < path.size() - 1) System.out.print(" -> ");
                if ((i + 1) % 3 == 0 && i < path.size() - 1) System.out.println();
            }
            System.out.println();
            System.out.println("─".repeat(50));
            System.out.printf("+ Total Waktu  : %d menit%n",   totalWaktu);
            System.out.printf("+ Total Jarak  : %.2f km%n",     totalJarak);
            System.out.printf("+ Total Biaya  : Rp %,d%n",      totalBiaya);
            System.out.printf("+ Jumlah Stop  : %d node%n",     path.size());
            System.out.println("─".repeat(50));
        }

        @Override
        public String toString() {
            return "Path=" + path + " | Waktu=" + totalWaktu
                    + " min | Jarak=" + totalJarak
                    + " km | Biaya=Rp " + totalBiaya;
        }
    }
}
