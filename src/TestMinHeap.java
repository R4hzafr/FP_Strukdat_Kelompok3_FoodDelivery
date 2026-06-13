import tree.MinHeap;
import model.Order;
import model.CSVLoader;
import java.util.*;

public class TestMinHeap {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TEST MINHEAP - URUTAN PENGANTARAN");
        System.out.println("========================================\n");
        
        // Load orders dari CSV
        List<Order> orders = CSVLoader.loadOrders("data/orders.csv");
        
        // Buat MinHeap
        MinHeap heap = new MinHeap();
        
        // Insert semua orders ke MinHeap
        System.out.println("\n--- Menambahkan semua orders ke MinHeap ---");
        for (Order order : orders) {
            heap.enqueue(order);
        }
        
        // Tampilkan heap
        heap.displayAll();
        
        // Pop orders satu-satu (urutan prioritas)
        System.out.println("\n--- Urutan Pengantaran (Pop dari MinHeap) ---");
        int deliveryNum = 1;
        while (!heap.isEmpty()) {
            Order nextOrder = heap.dequeue();
            System.out.println(deliveryNum + ". DELIVER: " + nextOrder);
            deliveryNum++;
        }
        
        // Test VIP Order mendadak
        System.out.println("\n--- Test VIP ORDER MASUK MENDADAK ---");
        MinHeap heap2 = new MinHeap();
        
        // Insert beberapa normal order
        heap2.enqueue(new Order("ORD101", "R1", "C1", 25, "Normal", "Pending"));
        heap2.enqueue(new Order("ORD102", "R2", "C2", 20, "Normal", "Pending"));
        heap2.enqueue(new Order("ORD103", "R3", "C3", 30, "High", "Pending"));
        
        heap2.displayAll();
        
        // VIP order masuk
        Order vipOrder = new Order("ORD_VIP", "R4", "C4", 5, "Urgent", "Pending");
        heap2.insertVIPOrder(vipOrder);
        
        heap2.displayAll();
        
        System.out.println("\n========================================");
        System.out.println("✅ TEST MINHEAP SELESAI");
        System.out.println("========================================");
    }
}