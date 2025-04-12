package main;

import model.*;
import service.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TravelService travelService = new TravelService();

        Airport airport1 = new Airport("OTP", "Otopeni", "București");
        Airport airport2 = new Airport("CDG", "Charles de Gaulle", "Paris");

        travelService.addDestination("Paris", "Franța");
        travelService.addDestination("Roma", "Italia");

        travelService.createFlight("RO123", airport1, airport2, "2025-05-01 10:00", "2025-05-01 12:30");

        Client client1 = new Client("John", "Doe", "john.doe@example.com");

        Flight flight = travelService.getFlight("RO123");
        if (flight != null) {
            FlightReservation flightReservation = travelService.createFlightReservation(client1, flight);
            System.out.println(flightReservation);
        }

        Destination hotelDestination = new Destination("Franța", "Paris");
        Hotel hotel = new Hotel("Hotel Paris", 4, hotelDestination);

        ExtraService service1 = new ExtraService(ExtraServiceType.LUGGAGE, 50.0);
        ExtraService service2 = new ExtraService(ExtraServiceType.TRAVEL_INSURANCE, 20.0);

        VacationPackage vacationPackage = new VacationPackage("Tropical Beach Getaway", hotel, flight, Arrays.asList(service1, service2), 1500.0);

        VacationPackageReservation vacationReservation = travelService.createVacationPackageReservation(client1, vacationPackage);
        System.out.println(vacationReservation);

        System.out.println("Available Destinations:");
        for (Destination destination : travelService.getAvailableDestinations()) {
            System.out.println(destination);
        }
    }
}
