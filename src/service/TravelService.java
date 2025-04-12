package service;

import model.*;

import java.util.*;

public class TravelService {

    private List<Flight> flights;
    private TreeSet<Destination> destinations;

    public TravelService() {
        flights = new ArrayList<>();
        destinations = new TreeSet<>(Comparator.comparing(Destination::getCity));
    }

    public void createFlight(String flightCode, Airport departure, Airport arrival, String departureDate, String arrivalDate) {
        Flight flight = new Flight(flightCode, departure, arrival, departureDate, arrivalDate);
        flights.add(flight);
        System.out.println("Flight added: " + flight);
    }

    public void addDestination(String city, String country) {
        Destination destination = new Destination(country, city);
        destinations.add(destination);
        System.out.println("Destination added: " + destination);
    }

    public Set<Destination> getAvailableDestinations() {
        return destinations;
    }

    public Flight getFlight(String flightCode) {
        return flights.stream()
                .filter(f -> f.getFlightCode().equals(flightCode))
                .findFirst()
                .orElse(null);
    }

    public FlightReservation createFlightReservation(Client client, Flight flight) {
        FlightReservation reservation = new FlightReservation(client, flight);
        System.out.println("Flight reservation created: " + reservation);
        return reservation;
    }

    public VacationPackageReservation createVacationPackageReservation(Client client, VacationPackage vacationPackage) {
        VacationPackageReservation reservation = new VacationPackageReservation(client, vacationPackage);
        System.out.println("Vacation Package Reservation created: " + reservation);
        return reservation;
    }
}
