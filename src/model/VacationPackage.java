package model;

import java.util.List;

public class VacationPackage {
    private String packageName;
    private Hotel hotel;
    private Flight flight;
    private List<ExtraService> services;
    private double totalPrice;

    public VacationPackage(String packageName, Hotel hotel, Flight flight, List<ExtraService> services, double totalPrice) {
        this.packageName = packageName;
        this.hotel = hotel;
        this.flight = flight;
        this.services = services;
        this.totalPrice = totalPrice;
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

    @Override
    public String toString() {
        return packageName + ": " + hotel + " + " + flight + " + " + services.size() + " extra services - $" + totalPrice;
    }
}
