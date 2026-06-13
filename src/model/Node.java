package model;

public class Node {
    public String nodeId;
    public String nodeType;      // "Restaurant", "Customer", "Simpang"
    public String namaLokasi;
    public double latitude;
    public double longitude;

    public Node(String nodeId, String nodeType, String namaLokasi, double latitude, double longitude) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.namaLokasi = namaLokasi;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return nodeId + " (" + namaLokasi + ")";
    }
}