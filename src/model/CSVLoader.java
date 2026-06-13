package model;

import java.io.*;
import java.util.*;

public class CSVLoader {
    
    // Baca nodes.csv dan return HashMap
    public static Map<String, Node> loadNodes(String filePath) {
        Map<String, Node> nodes = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            
            while ((line = br.readLine()) != null) {
                lineNum++;
                
                // Skip header (baris pertama)
                if (lineNum == 1) continue;
                
                String[] data = line.split(",");
                
                if (data.length >= 5) {
                    String nodeId = data[0].trim();
                    String nodeType = data[1].trim();
                    String namaLokasi = data[2].trim();
                    double latitude = Double.parseDouble(data[3].trim());
                    double longitude = Double.parseDouble(data[4].trim());
                    
                    Node node = new Node(nodeId, nodeType, namaLokasi, latitude, longitude);
                    nodes.put(nodeId, node);
                }
            }
            
            System.out.println("✓ Loaded " + nodes.size() + " nodes from " + filePath);
            
        } catch (IOException e) {
            System.err.println("❌ Error loading nodes: " + e.getMessage());
        }
        
        return nodes;
    }
    
    // Baca edges.csv dan return Map<String, List<Edge>>
    public static Map<String, List<Edge>> loadEdges(String filePath) {
        Map<String, List<Edge>> edges = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            
            while ((line = br.readLine()) != null) {
                lineNum++;
                
                // Skip header
                if (lineNum == 1) continue;
                
                String[] data = line.split(",");
                
                if (data.length >= 7) {
                    String source = data[0].trim();
                    String destination = data[1].trim();
                    int waktuTempuh = Integer.parseInt(data[2].trim());
                    double jarak = Double.parseDouble(data[3].trim());
                    String statusJalan = data[4].trim();
                    int biaya = Integer.parseInt(data[5].trim());
                    double ratingJalan = Double.parseDouble(data[6].trim());
                    
                    Edge edge = new Edge(destination, waktuTempuh, jarak, statusJalan, biaya, ratingJalan);
                    
                    edges.putIfAbsent(source, new ArrayList<>());
                    edges.get(source).add(edge);
                }
            }
            
            System.out.println("✓ Loaded edges from " + filePath);
            
        } catch (IOException e) {
            System.err.println("❌ Error loading edges: " + e.getMessage());
        }
        
        return edges;
    }
    
    // Baca orders.csv dan return List<Order>
    public static List<Order> loadOrders(String filePath) {
        List<Order> orders = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            
            while ((line = br.readLine()) != null) {
                lineNum++;
                
                // Skip header
                if (lineNum == 1) continue;
                
                String[] data = line.split(",");
                
                if (data.length >= 6) {
                    String orderId = data[0].trim();
                    String restaurantNode = data[1].trim();
                    String customerNode = data[2].trim();
                    int deadlineMenit = Integer.parseInt(data[3].trim());
                    String priorityLevel = data[4].trim();
                    String statusOrder = data[5].trim();
                    
                    Order order = new Order(orderId, restaurantNode, customerNode, 
                                           deadlineMenit, priorityLevel, statusOrder);
                    orders.add(order);
                }
            }
            
            System.out.println("✓ Loaded " + orders.size() + " orders from " + filePath);
            
        } catch (IOException e) {
            System.err.println("❌ Error loading orders: " + e.getMessage());
        }
        
        return orders;
    }
}