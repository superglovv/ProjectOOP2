package main;

import model.*;
import service.TravelService;
import util.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        DatabaseService.getInstance();

        TravelService travelService = new TravelService();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            travelService.displayMenu();
            choice = travelService.readInt(scanner, "Your choice: ");

            switch (choice) {
                case 1: // Add Client
                    String clientFirstName = travelService.readString(scanner, "Enter client first name: ");
                    String clientLastName = travelService.readString(scanner, "Enter client last name: ");
                    String clientEmail = travelService.readString(scanner, "Enter client email: ");
                    travelService.addClient(new Client(clientFirstName, clientLastName, clientEmail));
                    break;
                case 2: // View All Clients
                    travelService.getAllClients();
                    break;
                case 3: // Update Client
                    int updateClientId = travelService.readInt(scanner, "Enter client ID to update: ");
                    Client existingClient = travelService.getClientById(updateClientId);
                    if (existingClient != null) {
                        String newFirstName = travelService.readString(scanner, "Enter new first name (current: " + existingClient.getFirstName() + "): ");
                        String newLastName = travelService.readString(scanner, "Enter new last name (current: " + existingClient.getLastName() + "): ");
                        String newEmail = travelService.readString(scanner, "Enter new email (current: " + existingClient.getEmail() + "): ");
                        existingClient.setFirstName(newFirstName);
                        existingClient.setLastName(newLastName);
                        existingClient.setEmail(newEmail);
                        travelService.updateClient(existingClient);
                    }
                    break;
                case 4: // Delete Client
                    int deleteClientId = travelService.readInt(scanner, "Enter client ID to delete: ");
                    travelService.deleteClient(deleteClientId);
                    break;
                case 5: // Add Flight
                    String flightCode = travelService.readString(scanner, "Enter flight code: ");
                    String depAirportCode = travelService.readString(scanner, "Enter departure airport code: ");
                    String depAirportName = travelService.readString(scanner, "Enter departure airport name: ");
                    String depAirportCity = travelService.readString(scanner, "Enter departure airport city: ");
                    Airport departureAirport = new Airport(depAirportCode, depAirportName, depAirportCity);

                    String arrAirportCode = travelService.readString(scanner, "Enter arrival airport code: ");
                    String arrAirportName = travelService.readString(scanner, "Enter arrival airport name: ");
                    String arrAirportCity = travelService.readString(scanner, "Enter arrival airport city: ");
                    Airport arrivalAirport = new Airport(arrAirportCode, arrAirportName, arrAirportCity);

                    String departureDate = travelService.readString(scanner, "Enter departure date (YYYY-MM-DD HH:MM:SS): ");
                    String arrivalDate = travelService.readString(scanner, "Enter arrival date (YYYY-MM-DD HH:MM:SS): ");

                    travelService.addFlight(new Flight(flightCode, departureAirport, arrivalAirport, departureDate, arrivalDate));
                    break;
                case 6: // View All Flights
                    travelService.getAllFlights();
                    break;
                case 7: // Update Flight
                    int updateFlightId = travelService.readInt(scanner, "Enter flight ID to update: ");
                    Flight existingFlight = travelService.getFlightById(updateFlightId);
                    if (existingFlight != null) {
                        System.out.println("Updating flight with ID: " + existingFlight.getId());
                        String newDepDate = travelService.readString(scanner, "Enter new departure date (current: " + existingFlight.getDepartureDate() + "): ");
                        String newArrDate = travelService.readString(scanner, "Enter new arrival date (current: " + existingFlight.getArrivalDate() + "): ");
                        existingFlight.setDepartureDate(newDepDate);
                        existingFlight.setArrivalDate(newArrDate);

                        String newDepAirportCode = travelService.readString(scanner, "Enter new departure airport code (current: " + existingFlight.getDepartureAirport().getCode() + "): ");
                        String newDepAirportName = travelService.readString(scanner, "Enter new departure airport name (current: " + existingFlight.getDepartureAirport().getName() + "): ");
                        String newDepAirportCity = travelService.readString(scanner, "Enter new departure airport city (current: " + existingFlight.getDepartureAirport().getCity() + "): ");
                        existingFlight.setDepartureAirport(new Airport(newDepAirportCode, newDepAirportName, newDepAirportCity));

                        String newArrAirportCode = travelService.readString(scanner, "Enter new arrival airport code (current: " + existingFlight.getArrivalAirport().getCode() + "): ");
                        String newArrAirportName = travelService.readString(scanner, "Enter new arrival airport name (current: " + existingFlight.getArrivalAirport().getName() + "): ");
                        String newArrAirportCity = travelService.readString(scanner, "Enter new arrival airport city (current: " + existingFlight.getArrivalAirport().getCity() + "): ");
                        existingFlight.setArrivalAirport(new Airport(newArrAirportCode, newArrAirportName, newArrAirportCity));

                        travelService.updateFlight(existingFlight);
                    }
                    break;
                case 8: // Delete Flight
                    int deleteFlightId = travelService.readInt(scanner, "Enter flight ID to delete: ");
                    travelService.deleteFlight(deleteFlightId);
                    break;
                case 9: // Add Hotel
                    String hotelName = travelService.readString(scanner, "Enter hotel name: ");
                    int hotelStars = travelService.readInt(scanner, "Enter hotel stars (1-5): ");
                    String destCountry = travelService.readString(scanner, "Enter destination country for hotel: ");
                    String destCity = travelService.readString(scanner, "Enter destination city for hotel: ");
                    Destination hotelLocation = new Destination(destCountry, destCity);
                    travelService.addHotel(new Hotel(hotelName, hotelStars, hotelLocation));
                    break;
                case 10: // View All Hotels
                    travelService.getAllHotels();
                    break;
                case 11: // Update Hotel
                    int updateHotelId = travelService.readInt(scanner, "Enter hotel ID to update: ");
                    Hotel existingHotel = travelService.getHotelById(updateHotelId);
                    if (existingHotel != null) {
                        System.out.println("Updating hotel with ID: " + existingHotel.getId());
                        String newHotelName = travelService.readString(scanner, "Enter new hotel name (current: " + existingHotel.getName() + "): ");
                        int newStars = travelService.readInt(scanner, "Enter new stars (current: " + existingHotel.getStars() + "): ");
                        existingHotel.setName(newHotelName);
                        existingHotel.setStars(newStars);
                        String newDestCountry = travelService.readString(scanner, "Enter new destination country (current: " + existingHotel.getLocation().getCountry() + "): ");
                        String newDestCity = travelService.readString(scanner, "Enter new destination city (current: " + existingHotel.getLocation().getCity() + "): ");
                        existingHotel.setLocation(new Destination(newDestCountry, newDestCity));

                        travelService.updateHotel(existingHotel);
                    }
                    break;
                case 12: // Delete Hotel
                    int deleteHotelId = travelService.readInt(scanner, "Enter hotel ID to delete: ");
                    travelService.deleteHotel(deleteHotelId);
                    break;
                case 13: // Add Vacation Package
                    String packageName = travelService.readString(scanner, "Enter package name: ");

                    int hotelPkgId = travelService.readInt(scanner, "Enter Hotel ID for package (0 to create new): ");
                    Hotel selectedHotel = null;
                    if (hotelPkgId == 0) {
                        String newHotelName = travelService.readString(scanner, "Enter NEW hotel name: ");
                        int newHotelStars = travelService.readInt(scanner, "Enter NEW hotel stars (1-5): ");
                        String newDestCountry = travelService.readString(scanner, "Enter NEW destination country for hotel: ");
                        String newDestCity = travelService.readString(scanner, "Enter NEW destination city for hotel: ");
                        Destination newHotelLocation = new Destination(newDestCountry, newDestCity);
                        selectedHotel = new Hotel(newHotelName, newHotelStars, newHotelLocation);
                    } else {
                        selectedHotel = travelService.getHotelById(hotelPkgId);
                        if (selectedHotel == null) {
                            System.out.println("Hotel not found. Please try again.");
                            break;
                        }
                    }

                    int flightPkgId = travelService.readInt(scanner, "Enter Flight ID for package (0 to create new): ");
                    Flight selectedFlight = null;
                    if (flightPkgId == 0) {
                        String newFlightCode = travelService.readString(scanner, "Enter NEW flight code: ");
                        String newDepAirportCode = travelService.readString(scanner, "Enter NEW departure airport code: ");
                        String newDepAirportName = travelService.readString(scanner, "Enter NEW departure airport name: ");
                        String newDepAirportCity = travelService.readString(scanner, "Enter NEW departure airport city: ");
                        Airport newDepartureAirport = new Airport(newDepAirportCode, newDepAirportName, newDepAirportCity);

                        String newArrAirportCode = travelService.readString(scanner, "Enter NEW arrival airport code: ");
                        String newArrAirportName = travelService.readString(scanner, "Enter NEW arrival airport name: ");
                        String newArrAirportCity = travelService.readString(scanner, "Enter NEW arrival airport city: ");
                        Airport newArrivalAirport = new Airport(newArrAirportCode, newArrAirportName, newArrAirportCity);

                        String newDepartureDate = travelService.readString(scanner, "Enter NEW departure date (YYYY-MM-DD HH:MM:SS): ");
                        String newArrivalDate = travelService.readString(scanner, "Enter NEW arrival date (YYYY-MM-DD HH:MM:SS): ");
                        selectedFlight = new Flight(newFlightCode, newDepartureAirport, newArrivalAirport, newDepartureDate, newArrivalDate);
                    } else {
                        selectedFlight = travelService.getFlightById(flightPkgId);
                        if (selectedFlight == null) {
                            System.out.println("Flight not found. Please try again.");
                            break;
                        }
                    }

                    List<ExtraService> services = new ArrayList<>();
                    String addServiceChoice;
                    do {
                        addServiceChoice = travelService.readString(scanner, "Add an extra service to package? (yes/no): ").toLowerCase();
                        if ("yes".equals(addServiceChoice)) {
                            System.out.println("Available Extra Service Types: " + java.util.Arrays.toString(ExtraServiceType.values()));
                            String serviceTypeName = travelService.readString(scanner, "Enter service type (e.g., CAR_RENTAL, TRAVEL_INSURANCE): ").toUpperCase();
                            try {
                                ExtraServiceType type = ExtraServiceType.valueOf(serviceTypeName);
                                double servicePrice = travelService.readDouble(scanner, "Enter service price: ");
                                services.add(new ExtraService(type, servicePrice));
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid service type. Please choose from available types.");
                            }
                        }
                    } while ("yes".equals(addServiceChoice));

                    double totalPrice = travelService.readDouble(scanner, "Enter total price for the package: ");

                    travelService.addVacationPackage(new VacationPackage(packageName, selectedHotel, selectedFlight, services, totalPrice));
                    break;
                case 14: // View All Vacation Packages
                    travelService.getAllVacationPackages();
                    break;
                case 15: // Update Vacation Package
                    int updatePackageId = travelService.readInt(scanner, "Enter vacation package ID to update: ");
                    VacationPackage existingPackage = travelService.getVacationPackageById(updatePackageId);
                    if (existingPackage != null) {
                        System.out.println("Updating vacation package with ID: " + existingPackage.getId());
                        String newPackageName = travelService.readString(scanner, "Enter new package name (current: " + existingPackage.getPackageName() + "): ");
                        existingPackage.setPackageName(newPackageName);

                        int newHotelId = travelService.readInt(scanner, "Enter new Hotel ID for package (current: " + existingPackage.getHotel().getId() + ", 0 to create new): ");
                        Hotel updatedHotel = null;
                        if (newHotelId == 0) {
                            String newHName = travelService.readString(scanner, "Enter NEW hotel name: ");
                            int newHStars = travelService.readInt(scanner, "Enter NEW hotel stars (1-5): ");
                            String newHDestCountry = travelService.readString(scanner, "Enter NEW destination country for hotel: ");
                            String newHDestCity = travelService.readString(scanner, "Enter NEW destination city for hotel: ");
                            updatedHotel = new Hotel(newHName, newHStars, new Destination(newHDestCountry, newHDestCity));
                        } else {
                            updatedHotel = travelService.getHotelById(newHotelId);
                            if (updatedHotel == null) {
                                System.out.println("Hotel not found. Keeping existing hotel.");
                                updatedHotel = existingPackage.getHotel(); // Revert to old hotel if new not found
                            }
                        }
                        existingPackage.setHotel(updatedHotel);

                        int newFlightId = travelService.readInt(scanner, "Enter new Flight ID for package (current: " + existingPackage.getFlight().getId() + ", 0 to create new): ");
                        Flight updatedFlight = null;
                        if (newFlightId == 0) {
                            String newFCode = travelService.readString(scanner, "Enter NEW flight code: ");
                            String newFDepAirportCode = travelService.readString(scanner, "Enter NEW departure airport code: ");
                            String newFDepAirportName = travelService.readString(scanner, "Enter NEW departure airport name: ");
                            String newFDepAirportCity = travelService.readString(scanner, "Enter NEW departure airport city: ");
                            Airport newFDepartureAirport = new Airport(newFDepAirportCode, newFDepAirportName, newFDepAirportCity);

                            String newFArrAirportCode = travelService.readString(scanner, "Enter NEW arrival airport code: ");
                            String newFArrAirportName = travelService.readString(scanner, "Enter NEW arrival airport name: ");
                            String newFArrAirportCity = travelService.readString(scanner, "Enter NEW arrival airport city: ");
                            Airport newFArrivalAirport = new Airport(newFArrAirportCode, newFArrAirportName, newFArrAirportCity);

                            String newFDepartureDate = travelService.readString(scanner, "Enter NEW departure date (YYYY-MM-DD HH:MM:SS): ");
                            String newFArrivalDate = travelService.readString(scanner, "Enter NEW arrival date (YYYY-MM-DD HH:MM:SS): ");
                            updatedFlight = new Flight(newFCode, newFDepartureAirport, newFArrivalAirport, newFDepartureDate, newFArrivalDate);
                        } else {
                            updatedFlight = travelService.getFlightById(newFlightId);
                            if (updatedFlight == null) {
                                System.out.println("Flight not found. Keeping existing flight.");
                                updatedFlight = existingPackage.getFlight(); // Revert to old flight if new not found
                            }
                        }
                        existingPackage.setFlight(updatedFlight);

                        List<ExtraService> newServices = new ArrayList<>();
                        String updateServiceChoice;
                        do {
                            updateServiceChoice = travelService.readString(scanner, "Add an extra service to package? (yes/no): ").toLowerCase();
                            if ("yes".equals(updateServiceChoice)) {
                                System.out.println("Available Extra Service Types: " + java.util.Arrays.toString(ExtraServiceType.values()));
                                String serviceTypeName = travelService.readString(scanner, "Enter service type (e.g., CAR_RENTAL, TRAVEL_INSURANCE): ").toUpperCase();
                                try {
                                    ExtraServiceType type = ExtraServiceType.valueOf(serviceTypeName);
                                    double servicePrice = travelService.readDouble(scanner, "Enter service price: ");
                                    newServices.add(new ExtraService(type, servicePrice));
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid service type. Please choose from available types.");
                                }
                            }
                        } while ("yes".equals(updateServiceChoice));
                        existingPackage.setServices(newServices);

                        double newTotalPrice = travelService.readDouble(scanner, "Enter new total price (current: " + existingPackage.getTotalPrice() + "): ");
                        existingPackage.setTotalPrice(newTotalPrice);

                        travelService.updateVacationPackage(existingPackage);
                    }
                    break;
                case 16: // Delete Vacation Package
                    int deletePackageId = travelService.readInt(scanner, "Enter vacation package ID to delete: ");
                    travelService.deleteVacationPackage(deletePackageId);
                    break;
                case 17: // Create Flight Reservation (placeholder)
                    int clientIdForFlightRes = travelService.readInt(scanner, "Enter client ID for flight reservation: ");
                    Client resClientFlight = travelService.getClientById(clientIdForFlightRes);
                    if (resClientFlight == null) {
                        System.out.println("Client not found.");
                        break;
                    }
                    int flightIdForRes = travelService.readInt(scanner, "Enter Flight ID for reservation: ");
                    Flight resFlight = travelService.getFlightById(flightIdForRes);
                    if (resFlight == null) {
                        System.out.println("Flight not found.");
                        break;
                    }
                    travelService.createFlightReservation(resClientFlight, resFlight);
                    break;
                case 18: // Create Vacation Package Reservation (placeholder)
                    int clientIdForVPRes = travelService.readInt(scanner, "Enter client ID for vacation package reservation: ");
                    Client resClientVP = travelService.getClientById(clientIdForVPRes);
                    if (resClientVP == null) {
                        System.out.println("Client not found.");
                        break;
                    }
                    int packageIdForRes = travelService.readInt(scanner, "Enter Vacation Package ID for reservation: ");
                    VacationPackage resPackage = travelService.getVacationPackageById(packageIdForRes);
                    if (resPackage == null) {
                        System.out.println("Vacation Package not found.");
                        break;
                    }
                    travelService.createVacationPackageReservation(resClientVP, resPackage);
                    break;
                case 0:
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
        DatabaseService.getInstance().closeConnection();
    }
}