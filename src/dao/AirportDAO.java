package dao;

import model.Airport;
import util.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseService.getInstance().getConnection();
    }

    public int addAirport(Airport airport) {
        String SQL = "INSERT INTO airports (code, name, city) VALUES (?, ?, ?)";
        int id = -1;
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) { // Doar PreparedStatement în try-with-resources

                pstmt.setString(1, airport.getCode());
                pstmt.setString(2, airport.getName());
                pstmt.setString(3, airport.getCity());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                            airport.setId(id);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().startsWith("23")) {
                System.out.println("Airport with code '" + airport.getCode() + "' already exists. Retrieving existing ID.");
                Airport existingAirport = getAirportByCode(airport.getCode());
                if (existingAirport != null) {
                    id = existingAirport.getId();
                    airport.setId(id);
                }
            } else {
                System.err.println("Error adding airport: " + ex.getMessage());
            }
        }
        return id;
    }

    public Airport getAirportById(int id) {
        String SQL = "SELECT id, code, name, city FROM airports WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Airport(rs.getInt("id"), rs.getString("code"), rs.getString("name"), rs.getString("city"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting airport by ID: " + ex.getMessage());
        }
        return null;
    }

    public Airport getAirportByCode(String code) {
        String SQL = "SELECT id, code, name, city FROM airports WHERE code = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, code);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Airport(rs.getInt("id"), rs.getString("code"), rs.getString("name"), rs.getString("city"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting airport by code: " + ex.getMessage());
        }
        return null;
    }

    public List<Airport> getAllAirports() {
        List<Airport> airports = new ArrayList<>();
        String SQL = "SELECT id, code, name, city FROM airports";
        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {

                while (rs.next()) {
                    airports.add(new Airport(rs.getInt("id"), rs.getString("code"), rs.getString("name"), rs.getString("city")));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all airports: " + ex.getMessage());
        }
        return airports;
    }

    public boolean updateAirport(Airport airport) {
        String SQL = "UPDATE airports SET name = ?, city = ? WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, airport.getName());
                pstmt.setString(2, airport.getCity());
                pstmt.setInt(3, airport.getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating airport: " + ex.getMessage());
        }
        return false;
    }

    public boolean deleteAirport(int id) {
        String SQL = "DELETE FROM airports WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting airport: " + ex.getMessage());

            if (ex.getSQLState().startsWith("23")) {
                System.err.println("Cannot delete airport due to existing flights referencing it. " +
                        "Please delete associated flights first.");
            }
        }
        return false;
    }
}