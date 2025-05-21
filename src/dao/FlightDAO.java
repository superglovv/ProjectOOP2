package dao;

import model.Airport;
import model.Flight;
import util.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    private AirportDAO airportDAO = new AirportDAO(); // Dependency

    private Connection getConnection() throws SQLException {
        return DatabaseService.getInstance().getConnection();
    }

    public int addFlight(Flight flight) {
        String SQL = "INSERT INTO flights (flight_code, departure_airport_id, arrival_airport_id, departure_date, arrival_date) VALUES (?, ?, ?, ?, ?)";
        int id = -1;

        int departureAirportId = airportDAO.addAirport(flight.getDepartureAirport());
        int arrivalAirportId = airportDAO.addAirport(flight.getArrivalAirport());

        if (departureAirportId == -1 || arrivalAirportId == -1) {
            System.err.println("Failed to get/add departure or arrival airport for flight. Cannot add flight.");
            return -1;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, flight.getFlightCode());
                pstmt.setInt(2, departureAirportId);
                pstmt.setInt(3, arrivalAirportId);
                pstmt.setString(4, flight.getDepartureDate());
                pstmt.setString(5, flight.getArrivalDate());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                            flight.setId(id);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
             if (ex.getSQLState().startsWith("23")) {
                System.out.println("Flight with code '" + flight.getFlightCode() + "' already exists. Retrieving existing ID.");
                Flight existingFlight = getFlightByCode(flight.getFlightCode());
                if (existingFlight != null) {
                    id = existingFlight.getId();
                    flight.setId(id);
                }
            } else {
                System.err.println("Error adding flight: " + ex.getMessage());
            }
        }
        return id;
    }

    public Flight getFlightById(int id) {
        String SQL = "SELECT id, flight_code, departure_airport_id, arrival_airport_id, departure_date, arrival_date FROM flights WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Airport departureAirport = airportDAO.getAirportById(rs.getInt("departure_airport_id"));
                        Airport arrivalAirport = airportDAO.getAirportById(rs.getInt("arrival_airport_id"));

                        if (departureAirport == null || arrivalAirport == null) {
                            System.err.println("Referenced airport not found for flight ID: " + id);
                            return null;
                        }
                        return new Flight(rs.getInt("id"), rs.getString("flight_code"), departureAirport, arrivalAirport,
                                rs.getString("departure_date"), rs.getString("arrival_date"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting flight by ID: " + ex.getMessage());
        }
        return null;
    }

    public Flight getFlightByCode(String flightCode) {
        String SQL = "SELECT id, flight_code, departure_airport_id, arrival_airport_id, departure_date, arrival_date FROM flights WHERE flight_code = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, flightCode);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Airport departureAirport = airportDAO.getAirportById(rs.getInt("departure_airport_id"));
                        Airport arrivalAirport = airportDAO.getAirportById(rs.getInt("arrival_airport_id"));

                        if (departureAirport == null || arrivalAirport == null) {
                            System.err.println("Referenced airport not found for flight code: " + flightCode);
                            return null;
                        }
                        return new Flight(rs.getInt("id"), rs.getString("flight_code"), departureAirport, arrivalAirport,
                                rs.getString("departure_date"), rs.getString("arrival_date"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting flight by code: " + ex.getMessage());
        }
        return null;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String SQL = "SELECT id, flight_code, departure_airport_id, arrival_airport_id, departure_date, arrival_date FROM flights";
        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {

                while (rs.next()) {
                    Airport departureAirport = airportDAO.getAirportById(rs.getInt("departure_airport_id"));
                    Airport arrivalAirport = airportDAO.getAirportById(rs.getInt("arrival_airport_id"));

                    if (departureAirport != null && arrivalAirport != null) {
                        flights.add(new Flight(rs.getInt("id"), rs.getString("flight_code"), departureAirport, arrivalAirport,
                                rs.getString("departure_date"), rs.getString("arrival_date")));
                    } else {
                        System.err.println("Skipping flight " + rs.getString("flight_code") + " due to missing airport reference. (IDs: " + rs.getInt("departure_airport_id") + ", " + rs.getInt("arrival_airport_id") + ")");
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all flights: " + ex.getMessage());
        }
        return flights;
    }

    public boolean updateFlight(Flight flight) {
        String SQL = "UPDATE flights SET departure_airport_id = ?, arrival_airport_id = ?, departure_date = ?, arrival_date = ? WHERE id = ?";

        int departureAirportId = airportDAO.addAirport(flight.getDepartureAirport());
        int arrivalAirportId = airportDAO.addAirport(flight.getArrivalAirport());

        if (departureAirportId == -1 || arrivalAirportId == -1) {
            System.err.println("Failed to get/add departure or arrival airport for flight update. Cannot update flight.");
            return false;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, departureAirportId);
                pstmt.setInt(2, arrivalAirportId);
                pstmt.setString(3, flight.getDepartureDate());
                pstmt.setString(4, flight.getArrivalDate());
                pstmt.setInt(5, flight.getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating flight: " + ex.getMessage());
        }
        return false;
    }

    public boolean deleteFlight(int id) {
        String SQL = "DELETE FROM flights WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting flight: " + ex.getMessage());
            if (ex.getSQLState().startsWith("23")) {
                System.err.println("Cannot delete flight (ID: " + id + ") due to existing reservations or vacation packages referencing it. " +
                        "Please delete associated reservations/packages first.");
            }
        }
        return false;
    }
}