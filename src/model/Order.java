package model;

public class Order implements Comparable<Order> {
    public String orderId;
    public String restaurantNode;
    public String customerNode;
    public int deadlineMenit;
    public String priorityLevel;   // "Normal", "High", "Urgent"
    public String statusOrder;     // "Pending", "OnRoute", "Delivered"

    public Order(String orderId, String restaurantNode, String customerNode, 
                 int deadlineMenit, String priorityLevel, String statusOrder) {
        this.orderId = orderId;
        this.restaurantNode = restaurantNode;
        this.customerNode = customerNode;
        this.deadlineMenit = deadlineMenit;
        this.priorityLevel = priorityLevel;
        this.statusOrder = statusOrder;
    }

    // Priority: Urgent > High > Normal
    // Deadline: Lebih kecil = lebih prioritas
    @Override
    public int compareTo(Order other) {
        int priorityValue = getPriorityValue(this.priorityLevel) - getPriorityValue(other.priorityLevel);
        if (priorityValue != 0) return -priorityValue;  // Negative = lebih prioritas
        return this.deadlineMenit - other.deadlineMenit;
    }

    private static int getPriorityValue(String priority) {
        switch (priority.toLowerCase()) {
            case "urgent": return 3;
            case "high": return 2;
            default: return 1;
        }
    }

    @Override
    public String toString() {
        return orderId + " | " + restaurantNode + " -> " + customerNode + 
               " | Priority: " + priorityLevel + " | Deadline: " + deadlineMenit + " min";
    }
}