package model;

public class Edge {
    public String destination;
    public int waktuTempuh;      // main weight
    public double jarak;
    public String statusJalan;
    public int biaya;
    public double ratingJalan;

    public Edge(String destination, int waktuTempuh, double jarak, String statusJalan, int biaya, double ratingJalan) {
        this.destination = destination;
        this.waktuTempuh = waktuTempuh;
        this.jarak = jarak;
        this.statusJalan = statusJalan;
        this.biaya = biaya;
        this.ratingJalan = ratingJalan;
    }

    @Override
    public String toString() {
        return destination + " (" + waktuTempuh + " min)";
    }
}