package service;

import model.*;
import dao.*;
import audit.AuditService;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TravelService {
    private ClientDAO clientDAO = new ClientDAO();
    private FlightDAO flightDAO = new FlightDAO();
    private HotelDAO hotelDAO = new HotelDAO();
    private VacationPackageDAO vacationPackageDAO = new VacationPackageDAO();
    private ExtraServiceDAO extraServiceDAO = new ExtraServiceDAO();

    private AuditService auditService = AuditService.getInstance();

    // --- Client Operations ---
    public void addClient(Client client) {
        auditService.logAction("ADD_CLIENT");
        int id = clientDAO.addClient(client);
        if (id != -1) {
            System.out.println("Client '" + client.getFirstName() + " " + client.getLastName() + "' added with ID: " + id);
        } else {
            System.out.println("Failed to add client: " + client.getEmail());
        }
    }

    public Client getClientById(int id) {
        auditService.logAction("GET_CLIENT_BY_ID");
        Client client = clientDAO.getClientById(id);
        if (client != null) {
            System.out.println("Retrieved client: " + client);
        } else {
            System.out.println("Client with ID " + id + " not found.");
        }
        return client;
    }

    public Client getClientByEmail(String email) {
        auditService.logAction("GET_CLIENT_BY_EMAIL");
        Client client = clientDAO.getClientByEmail(email);
        if (client != null) {
            System.out.println("Retrieved client by email: " + client);
        } else {
            System.out.println("Client with email " + email + " not found.");
        }
        return client;
    }

    public List<Client> getAllClients() {
        auditService.logAction("GET_ALL_CLIENTS");
        List<Client> clients = clientDAO.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            System.out.println("All clients:");
            clients.forEach(System.out::println);
        }
        return clients;
    }

    public void updateClient(Client client) {
        auditService.logAction("UPDATE_CLIENT");
        boolean success = clientDAO.updateClient(client);
        if (success) {
            System.out.println("Client with ID " + client.getId() + " updated successfully.");
        } else {
            System.out.println("Failed to update client with ID " + client.getId() + ".");
        }
    }

    public void deleteClient(int id) {
        auditService.logAction("DELETE_CLIENT");
        boolean success = clientDAO.deleteClient(id);
        if (success) {
            System.out.println("Client with ID " + id + " deleted successfully.");
        } else {
            System.out.println("Failed to delete client with ID " + id + ". Check for associated reservations.");
        }
    }

    // --- Flight Operations ---
    public void addFlight(Flight flight) {
        auditService.logAction("ADD_FLIGHT");
        int id = flightDAO.addFlight(flight);
        if (id != -1) {
            System.out.println("Flight '" + flight.getFlightCode() + "' added with ID: " + id);
        } else {
            System.out.println("Failed to add flight: " + flight.getFlightCode());
        }
    }

    public Flight getFlightById(int id) {
        auditService.logAction("GET_FLIGHT_BY_ID");
        Flight flight = flightDAO.getFlightById(id);
        if (flight != null) {
            System.out.println("Retrieved flight: " + flight);
        } else {
            System.out.println("Flight with ID " + id + " not found.");
        }
        return flight;
    }

    public Flight getFlightByCode(String code) {
        auditService.logAction("GET_FLIGHT_BY_CODE");
        Flight flight = flightDAO.getFlightByCode(code);
        if (flight != null) {
            System.out.println("Retrieved flight by code: " + flight);
        } else {
            System.out.println("Flight with code " + code + " not found.");
        }
        return flight;
    }

    public List<Flight> getAllFlights() {
        auditService.logAction("GET_ALL_FLIGHTS");
        List<Flight> flights = flightDAO.getAllFlights();
        if (flights.isEmpty()) {
            System.out.println("No flights found.");
        } else {
            System.out.println("All flights:");
            flights.forEach(System.out::println);
        }
        return flights;
    }

    public void updateFlight(Flight flight) {
        auditService.logAction("UPDATE_FLIGHT");
        boolean success = flightDAO.updateFlight(flight);
        if (success) {
            System.out.println("Flight with ID " + flight.getId() + " updated successfully.");
        } else {
            System.out.println("Failed to update flight with ID " + flight.getId() + ".");
        }
    }

    public void deleteFlight(int id) {
        auditService.logAction("DELETE_FLIGHT");
        boolean success = flightDAO.deleteFlight(id);
        if (success) {
            System.out.println("Flight with ID " + id + " deleted successfully.");
        } else {
            System.out.println("Failed to delete flight with ID " + id + ". Check for associated reservations/packages.");
        }
    }

    // --- Hotel Operations ---
    public void addHotel(Hotel hotel) {
        auditService.logAction("ADD_HOTEL");
        int id = hotelDAO.addHotel(hotel);
        if (id != -1) {
            System.out.println("Hotel '" + hotel.getName() + "' added with ID: " + id);
        } else {
            System.out.println("Failed to add hotel: " + hotel.getName());
        }
    }

    public Hotel getHotelById(int id) {
        auditService.logAction("GET_HOTEL_BY_ID");
        Hotel hotel = hotelDAO.getHotelById(id);
        if (hotel != null) {
            System.out.println("Retrieved hotel: " + hotel);
        } else {
            System.out.println("Hotel with ID " + id + " not found.");
        }
        return hotel;
    }

    public List<Hotel> getAllHotels() {
        auditService.logAction("GET_ALL_HOTELS");
        List<Hotel> hotels = hotelDAO.getAllHotels();
        if (hotels.isEmpty()) {
            System.out.println("No hotels found.");
        } else {
            System.out.println("All hotels:");
            hotels.forEach(System.out::println);
        }
        return hotels;
    }

    public void updateHotel(Hotel hotel) {
        auditService.logAction("UPDATE_HOTEL");
        boolean success = hotelDAO.updateHotel(hotel);
        if (success) {
            System.out.println("Hotel with ID " + hotel.getId() + " updated successfully.");
        } else {
            System.out.println("Failed to update hotel with ID " + hotel.getId() + ".");
        }
    }

    public void deleteHotel(int id) {
        auditService.logAction("DELETE_HOTEL");
        boolean success = hotelDAO.deleteHotel(id);
        if (success) {
            System.out.println("Hotel with ID " + id + " deleted successfully.");
        } else {
            System.out.println("Failed to delete hotel with ID " + id + ". Check for associated packages.");
        }
    }

    // --- Vacation Package Operations ---
    public void addVacationPackage(VacationPackage pkg) {
        auditService.logAction("ADD_VACATION_PACKAGE");
        int id = vacationPackageDAO.addVacationPackage(pkg);
        if (id != -1) {
            System.out.println("Vacation Package '" + pkg.getPackageName() + "' added with ID: " + id);
        } else {
            System.out.println("Failed to add vacation package: " + pkg.getPackageName());
        }
    }

    public VacationPackage getVacationPackageById(int id) {
        auditService.logAction("GET_VACATION_PACKAGE_BY_ID");
        VacationPackage pkg = vacationPackageDAO.getVacationPackageById(id);
        if (pkg != null) {
            System.out.println("Retrieved Vacation Package: " + pkg);
        } else {
            System.out.println("Vacation Package with ID " + id + " not found.");
        }
        return pkg;
    }

    public List<VacationPackage> getAllVacationPackages() {
        auditService.logAction("GET_ALL_VACATION_PACKAGES");
        List<VacationPackage> packages = vacationPackageDAO.getAllVacationPackages();
        if (packages.isEmpty()) {
            System.out.println("No vacation packages found.");
        } else {
            System.out.println("All Vacation Packages:");
            packages.forEach(System.out::println);
        }
        return packages;
    }

    public void updateVacationPackage(VacationPackage pkg) {
        auditService.logAction("UPDATE_VACATION_PACKAGE");
        boolean success = vacationPackageDAO.updateVacationPackage(pkg);
        if (success) {
            System.out.println("Vacation Package with ID " + pkg.getId() + " updated successfully.");
        } else {
            System.out.println("Failed to update Vacation Package with ID " + pkg.getId() + ".");
        }
    }

    public void deleteVacationPackage(int id) {
        auditService.logAction("DELETE_VACATION_PACKAGE");
        boolean success = vacationPackageDAO.deleteVacationPackage(id);
        if (success) {
            System.out.println("Vacation Package with ID " + id + " deleted successfully.");
        } else {
            System.out.println("Failed to delete Vacation Package with ID " + id + ". Check for associated reservations.");
        }
    }

    // --- Reservation Operations (simplified for now, DAO will be added later if needed) ---
    // Atentie: Aici vom avea nevoie de DAO-uri dedicate pentru Reservation, FlightReservation, VacationPackageReservation.
    // Pentru simplitate, momentan vom presupune că rezervările sunt gestionate prin adăugarea lor la un client (dacă e necesar)
    // sau pur și simplu le vom crea direct în Main pentru testare, fără persistență directă în DB.
    // Când vom face persistență pentru rezervări, vom avea ReservationDAO etc.
    // Până atunci, metodele de rezervare nu vor avea logări de audit la nivel de TravelService,
    // ci doar la nivel de creare a obiectelor Flight sau VacationPackage.

    // placeholder methods for demonstration of flow (not directly persisted yet)
    public void createFlightReservation(Client client, Flight flight) {
        auditService.logAction("CREATE_FLIGHT_RESERVATION");
        // In a real scenario, this would involve a ReservationDAO
        // int reservationId = reservationDAO.addFlightReservation(new FlightReservation(client, flight));
        // System.out.println("Flight reservation created for " + client.getFirstName() + " for flight " + flight.getFlightCode());
        System.out.println("Flight reservation for " + client.getFirstName() + " for flight " + flight.getFlightCode() + " processed.");
    }

    public void createVacationPackageReservation(Client client, VacationPackage pkg) {
        auditService.logAction("CREATE_VACATION_PACKAGE_RESERVATION");
        // In a real scenario, this would involve a ReservationDAO
        // int reservationId = reservationDAO.addVacationPackageReservation(new VacationPackageReservation(client, pkg));
        // System.out.println("Vacation package reservation created for " + client.getFirstName() + " for package " + pkg.getPackageName());
        System.out.println("Vacation package reservation for " + client.getFirstName() + " for package " + pkg.getPackageName() + " processed.");
    }

    // --- Other Methods (for demonstration) ---
    public void displayMenu() {
        System.out.println("\n--- Travel Agency Menu ---");
        System.out.println("1. Add Client");
        System.out.println("2. View All Clients");
        System.out.println("3. Update Client");
        System.out.println("4. Delete Client");
        System.out.println("--------------------");
        System.out.println("5. Add Flight");
        System.out.println("6. View All Flights");
        System.out.println("7. Update Flight");
        System.out.println("8. Delete Flight");
        System.out.println("--------------------");
        System.out.println("9. Add Hotel");
        System.out.println("10. View All Hotels");
        System.out.println("11. Update Hotel");
        System.out.println("12. Delete Hotel");
        System.out.println("--------------------");
        System.out.println("13. Add Vacation Package");
        System.out.println("14. View All Vacation Packages");
        System.out.println("15. Update Vacation Package");
        System.out.println("16. Delete Vacation Package");
        System.out.println("--------------------");
        System.out.println("17. Create Flight Reservation (placeholder)");
        System.out.println("18. Create Vacation Package Reservation (placeholder)");
        System.out.println("--------------------");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    public String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public double readDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}