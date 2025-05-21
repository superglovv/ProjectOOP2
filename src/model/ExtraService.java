package model;

public class ExtraService {
    private int id; // Adaugă acest câmp
    private ExtraServiceType type;
    private double price;

    // Constructor fără ID
    public ExtraService(ExtraServiceType type, double price) {
        this.type = type;
        this.price = price;
    }

    // Constructor cu ID
    public ExtraService(int id, ExtraServiceType type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public ExtraServiceType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setType(ExtraServiceType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ExtraService{" +
                "id=" + id +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}