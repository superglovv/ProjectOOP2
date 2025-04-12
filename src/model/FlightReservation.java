package model;

public class FlightReservation extends Reservation {
    private Flight flight;

    public FlightReservation(Client client, Flight flight) {
        super(client, "flight");
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }

    @Override
    public void displayDetails() {
        System.out.println("Flight reservation for " + client.getFirstName() + " " + client.getLastName() + ": " + flight);
    }
}
