package model;

import java.time.LocalDateTime;

public class FlightReservation extends Reservation {
    private Flight flight;

    public FlightReservation(Client client, Flight flight) {
        super(client); // Apelez constructorul clasei părinte (fără ID)
        this.flight = flight;
    }

    public FlightReservation(int id, Client client, LocalDateTime reservationDate, Flight flight) {
        super(id, client, reservationDate); // Apelez constructorul clasei părinte (cu ID)
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @Override
    public String getReservationType() {
        return "Flight";
    }

    @Override
    public String toString() {
        return "FlightReservation{" +
                "id=" + getId() + // Folosește getId() de la clasa părinte
                ", client=" + getClient().getFirstName() + " " + getClient().getLastName() +
                ", reservationDate=" + getReservationDate() +
                ", flight=" + flight.getFlightCode() +
                '}';
    }
}