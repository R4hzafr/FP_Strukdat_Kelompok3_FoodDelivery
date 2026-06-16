package graph;

import model.Node;
import model.Edge;
import model.CSVLoader;

import java.util.*;

public class Graph {

    private Map<String, List<Edge>> adjacencyList;
    private Map<String, Node> nodes;
    private Set<String> closedEdges;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.nodes         = new HashMap<>();
        this.closedEdges   = new HashSet<>();
    }

    public void loadFromCSV(String nodesPath, String edgesPath) {
        // Load nodesnya.
        nodes = CSVLoader.loadNodes(nodesPath);
        for (String id : nodes.keySet()) {
            adjacencyList.putIfAbsent(id, new ArrayList<>());
        }

        // Load edgesnya
        Map<String, List<Edge>> rawEdges = CSVLoader.loadEdges(edgesPath);
        for (Map.Entry<String, List<Edge>> entry : rawEdges.entrySet()) {
            String src = entry.getKey();
            for (Edge e : entry.getValue()) {
                addEdge(src, e);
                addEdge(e.destination,
                        new Edge(src, e.waktuTempuh, e.jarak,
                                 e.statusJalan, e.biaya, e.ratingJalan));
            }
        }

        System.out.println("✓ Graph built: " + nodes.size()
                + " nodes, " + countEdges() + " directed edges");
    }

    public void addNode(Node node) {
        nodes.put(node.nodeId, node);
        adjacencyList.putIfAbsent(node.nodeId, new ArrayList<>());
    }

    public void addEdge(String source, Edge edge) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.get(source).add(edge);
    }

    public void addUndirectedEdge(String src, String dst,
                                  int waktu, double jarak,
                                  String status, int biaya, double rating) {
        addEdge(src, new Edge(dst, waktu, jarak, status, biaya, rating));
        addEdge(dst, new Edge(src, waktu, jarak, status, biaya, rating));
    }

    public void closeEdge(String source, String destination) {
        closedEdges.add(edgeKey(source, destination));
        closedEdges.add(edgeKey(destination, source));
        System.out.println("- Jalan ditutup: " + source + " ↔ " + destination);
    }

    public void openEdge(String source, String destination) {
        closedEdges.remove(edgeKey(source, destination));
        closedEdges.remove(edgeKey(destination, source));
        System.out.println("+ Jalan dibuka: " + source + " ↔ " + destination);
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

    private boolean isEdgeActive(String src, String dst) {
        return !closedEdges.contains(edgeKey(src, dst));
    }

    private String edgeKey(String src, String dst) {
        return src + "_" + dst;
    }

    public ShortestPathResult dijkstra(String startId, String endId) {
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) {
            System.out.println("❌ Node tidak ditemukan: " + startId + " atau " + endId);
            return null;
        }

        Map<String, Integer> dist    = new HashMap<>();
        Map<String, String>  prev    = new HashMap<>();
        PriorityQueue<int[]> pq      = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        List<String>         nodeIds = new ArrayList<>(nodes.keySet());
        Map<String, Integer> idxMap  = new HashMap<>();
        for (int i = 0; i < nodeIds.size(); i++) idxMap.put(nodeIds.get(i), i);

        // Inisialisasi
        for (String id : nodes.keySet()) dist.put(id, Integer.MAX_VALUE);
        dist.put(startId, 0);
        pq.offer(new int[]{0, idxMap.getOrDefault(startId, 0)});


        PriorityQueue<String> queue = new PriorityQueue<>(
                Comparator.comparingInt(id -> dist.getOrDefault(id, Integer.MAX_VALUE)));
        queue.offer(startId);

        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (visited.contains(current)) continue;
            visited.add(current);

            if (current.equals(endId)) break;

            List<Edge> neighbors = adjacencyList.getOrDefault(current, Collections.emptyList());
            for (Edge edge : neighbors) {
                if (!isEdgeActive(current, edge.destination)) continue;

                int newDist = dist.get(current) + edge.waktuTempuh;
                if (newDist < dist.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
                    dist.put(edge.destination, newDist);
                    prev.put(edge.destination, current);
                    queue.offer(edge.destination);
                }
            }
        }

        if (dist.get(endId) == Integer.MAX_VALUE) {
            System.out.println("! Tidak ada jalur dari " + startId + " ke " + endId);
            return null;
        }

        List<String> path = new ArrayList<>();
        String cur = endId;
        while (cur != null) {
            path.add(0, cur);
            cur = prev.get(cur);
        }

        double totalJarak = 0;
        int    totalBiaya = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String s = path.get(i);
            String d = path.get(i + 1);
            Edge e = getEdge(s, d);
            if (e != null) {
                totalJarak += e.jarak;
                totalBiaya += e.biaya;
            }
        }

        return new ShortestPathResult(path, dist.get(endId), totalJarak, totalBiaya, nodes);
    }


    public boolean isConnected(String startId, String endId) {
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) {
            System.out.println("! Node tidak ditemukan.");
            return false;
        }

        Set<String>    visited = new HashSet<>();
        Queue<String>  queue   = new LinkedList<>();

        queue.offer(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(endId)) return true;

            for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(edge.destination)
                        && isEdgeActive(current, edge.destination)) {
                    visited.add(edge.destination);
                    queue.offer(edge.destination);
                }
            }
        }
        return false;
    }

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

    public void bfsLevelTraversal(String startId) {
        if (!nodes.containsKey(startId)) {
            System.out.println("! Node tidak ditemukan: " + startId);
            return;
        }

        System.out.println("\n--- BFS Level Traversal dari: "
                + startId + " (" + nodes.get(startId).namaLokasi + ")");
        System.out.println("─".repeat(50));

        Set<String>   visited = new LinkedHashSet<>();
        Queue<String> queue   = new LinkedList<>();
        Map<String, Integer> level = new HashMap<>();

        queue.offer(startId);
        visited.add(startId);
        level.put(startId, 0);

        while (!queue.isEmpty()) {
            String  current      = queue.poll();
            int     currentLevel = level.get(current);
            String  indent       = "  ".repeat(currentLevel);
            Node    n            = nodes.get(current);
            String  label        = (n != null) ? n.namaLokasi : current;

            System.out.println(indent + "Level " + currentLevel
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

        int total = visited.size();
        int disconnected = nodes.size() - total;
        System.out.println("─".repeat(50));
        System.out.println("Total node terjangkau: " + total + " / " + nodes.size());
        if (disconnected > 0) {
            System.out.println("!!!  " + disconnected + " node TIDAK terhubung dari " + startId);
        }
    }


    public void displayGraph() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║           ADJACENCY LIST — FOOD DELIVERY          ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        List<String> sortedIds = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sortedIds);

        for (String nodeId : sortedIds) {
            Node n = nodes.get(nodeId);
            String label = (n != null) ? n.namaLokasi : nodeId;
            System.out.printf("%-4s %-28s → ", nodeId, "(" + label + ")");

            List<Edge> edges = adjacencyList.get(nodeId);
            if (edges == null || edges.isEmpty()) {
                System.out.print("[no edges]");
            } else {
                StringJoiner sj = new StringJoiner(", ");
                for (Edge e : edges) {
                    boolean closed = !isEdgeActive(nodeId, e.destination);
                    String mark = closed ? "🚧" : "";
                    sj.add(mark + e.destination + "(" + e.waktuTempuh + "m)");
                }
                System.out.print(sj);
            }
            System.out.println();
        }
        System.out.println();
    }

    //Getter
    public Map<String, Node> getNodes()                          { return nodes; }
    public Map<String, List<Edge>> getAdjacencyList()           { return adjacencyList; }
    public Node getNode(String nodeId)                           { return nodes.get(nodeId); }
    public List<Edge> getNeighbors(String nodeId)               { return adjacencyList.getOrDefault(nodeId, Collections.emptyList()); }

    public Edge getEdge(String src, String dst) {
        for (Edge e : adjacencyList.getOrDefault(src, Collections.emptyList())) {
            if (e.destination.equals(dst) && isEdgeActive(src, dst)) return e;
        }
        return null;
    }

    public int getTotalNodes()  { return nodes.size(); }
    public int countEdges() {
        return adjacencyList.values().stream().mapToInt(List::size).sum();
    }

    public Set<String> getClosedEdges() { return Collections.unmodifiableSet(closedEdges); }

    public static class ShortestPathResult {
        public final List<String> path;
        public final int          totalWaktu; // menit
        public final double       totalJarak; // km
        public final int          totalBiaya; // rupiah
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

        //Format sesuai display.
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
                if (i < path.size() - 1) System.out.print(" → ");
                if ((i + 1) % 3 == 0 && i < path.size() - 1) System.out.println();
            }
            System.out.println();
            System.out.println("─".repeat(50));
            System.out.printf("⏱  Total Waktu  : %d menit%n",   totalWaktu);
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
