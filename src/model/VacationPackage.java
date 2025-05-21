package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Adaugă acest import pentru Objects.hash

public class VacationPackage {
    private int id; // Adaugă acest câmp
    private String packageName;
    private Hotel hotel;
    private Flight flight;
    private List<ExtraService> services;
    private double totalPrice;

    // Constructor fără ID
    public VacationPackage(String packageName, Hotel hotel, Flight flight, List<ExtraService> services, double totalPrice) {
        this.packageName = packageName;
        this.hotel = hotel;
        this.flight = flight;
        this.services = services != null ? services : new ArrayList<>();
        this.totalPrice = totalPrice;
    }

    // Constructor cu ID
    public VacationPackage(int id, String packageName, Hotel hotel, Flight flight, List<ExtraService> services, double totalPrice) {
        this.id = id;
        this.packageName = packageName;
        this.hotel = hotel;
        this.flight = flight;
        this.services = services != null ? services : new ArrayList<>();
        this.totalPrice = totalPrice;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<ExtraService> getServices() {
        return services;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setServices(List<ExtraService> services) {
        this.services = services;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VacationPackage{");
        sb.append("id=").append(id);
        sb.append(", packageName='").append(packageName).append('\'');
        sb.append(", hotel=").append(hotel != null ? hotel.getName() : "N/A");
        sb.append(", flight=").append(flight != null ? flight.getFlightCode() : "N/A");
        sb.append(", services=[");
        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                sb.append(services.get(i).getType().name());
                if (i < services.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("]");
        sb.append(", totalPrice=").append(totalPrice);
        sb.append('}');
        return sb.toString();
    }

    // Este o idee bună să ai equals și hashCode pentru clasele model, mai ales dacă le folosești în colecții.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacationPackage that = (VacationPackage) o;
        return id == that.id; // Poți folosi ID-ul pentru egalitate, sau o combinație de câmpuri unice
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}