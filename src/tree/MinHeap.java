package tree;

import model.Order;
import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private ArrayList<Order> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    public List<Order> getAll() { return new ArrayList<>(heap); }

    // Tambah order ke heap
    public void enqueue(Order order) {
        heap.add(order);
        heapifyUp(heap.size() - 1);
        System.out.println("Order " + order.orderId + " ditambahkan ke antrian (Priority: " + order.priorityLevel + ")");
    }

    // Ambil order dengan prioritas tertinggi
    public Order dequeue() {
        if (isEmpty()) {
            System.out.println("Heap kosong!");
            return null;
        }
        
        Order root = heap.get(0);
        Order last = heap.remove(heap.size() - 1);
        
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        
        return root;
    }

    // Lihat order pertama tanpa dihapus
    public Order peek() {
        if (isEmpty()) return null;
        return heap.get(0);
    }

    // Cek apakah heap kosong
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Ukuran heap
    public int size() {
        return heap.size();
    }

    // Naik ke atas (untuk insert)
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    // Turun ke bawah (untuk delete root)
    private void heapifyDown(int index) {
        while (true) {
            int smallest = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            if (leftChild < heap.size() && heap.get(leftChild).compareTo(heap.get(smallest)) < 0) {
                smallest = leftChild;
            }
            if (rightChild < heap.size() && heap.get(rightChild).compareTo(heap.get(smallest)) < 0) {
                smallest = rightChild;
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    // Tukar dua elemen
    private void swap(int i, int j) {
        Order temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Tampilkan semua order di heap
    public void displayAll() {
        System.out.println("\n=== URUTAN PENGANTARAN (MinHeap) ===");
        for (int i = 0; i < heap.size(); i++) {
            System.out.println((i + 1) + ". " + heap.get(i));
        }
    }

    // Handle VIP order yang masuk mendadak
    public void insertVIPOrder(Order vipOrder) {
        System.out.println("\n  VIP ORDER MASUK MENDADAK: " + vipOrder.orderId);
        enqueue(vipOrder);
        System.out.println(" VIP Order diprioritaskan!");
    }
}