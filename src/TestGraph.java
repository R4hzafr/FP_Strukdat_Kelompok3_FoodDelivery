//kalau mau testing pake ini ya

import graph.Graph;
import model.Node;

public class TestGraph {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║       TEST GRAPH — FOOD DELIVERY OPTIMIZER       ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // ── 1. Load Graph ──────────────────────────────────────────────────
        Graph graph = new Graph();
        graph.loadFromCSV("data/nodes.csv", "data/edges.csv");

        // ── 2. Tampilkan Adjacency List ────────────────────────────────────
        graph.displayGraph();

        // ── 3. BFS — cek keterhubungan ────────────────────────────────────
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("TEST BFS — CEK KETERHUBUNGAN AREA");
        System.out.println("════════════════════════════════════════════════════");

        String[][] bfsTests = {
            {"R1", "C12"},
            {"R4", "C9"},
            {"S1",  "S7"}
        };
        for (String[] pair : bfsTests) {
            boolean connected = graph.isConnected(pair[0], pair[1]);
            System.out.printf("  %s → %s : %s%n",
                    pair[0], pair[1],
                    connected ? "+ TERHUBUNG" : "- TIDAK TERHUBUNG");
        }

        // BFS Level Traversal dari S1 (simpang utama)
        graph.bfsLevelTraversal("S1");

        // ── 4. Dijkstra — rute tercepat ────────────────────────────────────
        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("TEST DIJKSTRA — RUTE TERCEPAT");
        System.out.println("════════════════════════════════════════════════════");

        String[][] dijkstraTests = {
            {"R1", "C1"},    // ORD001
            {"R4", "C10"},   // ORD010 (Urgent)
            {"R3", "C9"},    // ORD009
        };
        for (String[] pair : dijkstraTests) {
            System.out.println("\n! Rute Order: " + pair[0] + " → " + pair[1]);
            Graph.ShortestPathResult result = graph.dijkstra(pair[0], pair[1]);
            if (result != null) result.display();
        }

        // ── 5. Simulasi Jalan Tertutup ─────────────────────────────────────
        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("SIMULASI JALAN TERTUTUP — S1 ↔ S2");
        System.out.println("════════════════════════════════════════════════════");

        System.out.println("\n[SEBELUM] Rute R1 → C2:");
        Graph.ShortestPathResult before = graph.dijkstra("R1", "C2");
        if (before != null) before.display();

        // Tutup jalan S1 ↔ S2
        graph.closeEdge("S1", "S2");

        System.out.println("\n[SESUDAH jalan S1↔S2 ditutup] Rute R1 → C2:");
        Graph.ShortestPathResult after = graph.dijkstra("R1", "C2");
        if (after != null) after.display();
        else System.out.println("  ##  Tidak ada jalur alternatif!");

        // Buka kembali
        graph.openEdge("S1", "S2");

        // ── 6. BFS Reachable setelah simulasi ─────────────────────────────
        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("CEK REACHABLE dari R1 setelah jalan S1↔S3 ditutup");
        System.out.println("════════════════════════════════════════════════════");

        graph.closeEdge("S1", "S3");
        var reachable = graph.bfsReachable("R1");
        System.out.println("Node yang bisa dicapai dari R1: " + reachable);
        System.out.println("Total: " + reachable.size() + " / " + graph.getTotalNodes());
        graph.openEdge("S1", "S3");

        // ── 7. Edge Case ───────────────────────────────────────────────────
        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("EDGE CASE TESTS");
        System.out.println("════════════════════════════════════════════════════");

        // Node tidak exist
        System.out.println("\n[Edge Case 1] Node tidak ada:");
        graph.dijkstra("R99", "C1");

        // Source = Destination
        System.out.println("\n[Edge Case 2] Source == Destination:");
        Graph.ShortestPathResult sameNode = graph.dijkstra("R1", "R1");
        if (sameNode != null) sameNode.display();

        // Semua jalan dari S1 ditutup
        System.out.println("\n[Edge Case 3] Semua jalan dari R1 ditutup:");
        graph.closeEdge("R1", "S1");
        graph.closeEdge("R1", "R2");
        graph.closeEdge("R1", "C9");
        boolean reachC1 = graph.isConnected("R1", "C1");
        System.out.println("  R1 → C1 setelah semua edge ditutup: "
                + (reachC1 ? "+ TERHUBUNG" : "- TIDAK TERHUBUNG"));
        // Buka kembali
        graph.openEdge("R1", "S1");
        graph.openEdge("R1", "R2");
        graph.openEdge("R1", "C9");

        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("-- TEST GRAPH SELESAI –");
        System.out.println("════════════════════════════════════════════════════");
    }
}

