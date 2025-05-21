package model;

public class Flight {
    private int id; // Adaugă acest câmp
    private String flightCode;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private String departureDate; // Format "YYYY-MM-DD HH:MM:SS"
    private String arrivalDate;   // Format "YYYY-MM-DD HH:MM:SS"

    // Constructor fără ID
    public Flight(String flightCode, Airport departureAirport, Airport arrivalAirport, String departureDate, String arrivalDate) {
        this.flightCode = flightCode;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    // Constructor cu ID
    public Flight(int id, String flightCode, Airport departureAirport, Airport arrivalAirport, String departureDate, String arrivalDate) {
        this.id = id;
        this.flightCode = flightCode;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightCode='" + flightCode + '\'' +
                ", departureAirport=" + departureAirport.getCode() +
                ", arrivalAirport=" + arrivalAirport.getCode() +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                '}';
    }
}