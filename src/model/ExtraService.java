package model;

public class ExtraService {
    private ExtraServiceType type;
    private double price;

    public ExtraService(ExtraServiceType type, double price) {
        this.type = type;
        this.price = price;
    }

    public ExtraServiceType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return type.name() + " - $" + price;
    }
}
