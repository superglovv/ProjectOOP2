package model;

public class Flight {
    private String flightCode;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private String departureDate;
    private String arrivalDate;

    public Flight(String flightCode, Airport departureAirport, Airport arrivalAirport, String departureDate, String arrivalDate) {
        this.flightCode = flightCode;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
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

    @Override
    public String toString() {
        return "Flight " + flightCode + " from " + departureAirport + " to " + arrivalAirport;
    }
}
