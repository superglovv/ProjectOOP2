package dao;

import model.Destination;
import util.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinationDAO {

    private Connection getConnection() throws SQLException {

        return DatabaseService.getInstance().getConnection();
    }

    public int addDestination(Destination destination) {
        String SQL = "INSERT INTO destinations (country, city) VALUES (?, ?)";
        int id = -1;
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, destination.getCountry());
                pstmt.setString(2, destination.getCity());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) { // ResultSet în try-with-resources
                        if (rs.next()) {
                            id = rs.getInt(1);
                            destination.setId(id);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().startsWith("23")) {
                System.out.println("Destination '" + destination.getCity() + ", " + destination.getCountry() + "' already exists. Retrieving existing ID.");
                Destination existingDestination = getDestinationByCityAndCountry(destination.getCity(), destination.getCountry());
                if (existingDestination != null) {
                    id = existingDestination.getId();
                    destination.setId(id);
                }
            } else {
                System.err.println("Error adding destination: " + ex.getMessage());
            }
        }
        return id;
    }

    public Destination getDestinationById(int id) {
        String SQL = "SELECT id, country, city FROM destinations WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Destination(rs.getInt("id"), rs.getString("country"), rs.getString("city"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting destination by ID: " + ex.getMessage());
        }
        return null;
    }

    public Destination getDestinationByCityAndCountry(String city, String country) {
        String SQL = "SELECT id, country, city FROM destinations WHERE city = ? AND country = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, city);
                pstmt.setString(2, country);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Destination(rs.getInt("id"), rs.getString("country"), rs.getString("city"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting destination by city and country: " + ex.getMessage());
        }
        return null;
    }

    public List<Destination> getAllDestinations() {
        List<Destination> destinations = new ArrayList<>();
        String SQL = "SELECT id, country, city FROM destinations";
        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {

                while (rs.next()) {
                    destinations.add(new Destination(rs.getInt("id"), rs.getString("country"), rs.getString("city")));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all destinations: " + ex.getMessage());
        }
        return destinations;
    }

    public boolean updateDestination(Destination destination) {
        String SQL = "UPDATE destinations SET country = ?, city = ? WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                pstmt.setString(1, destination.getCountry());
                pstmt.setString(2, destination.getCity());
                pstmt.setInt(3, destination.getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating destination: " + ex.getMessage());
        }
        return false;
    }

    public boolean deleteDestination(int id) {
        String SQL = "DELETE FROM destinations WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting destination: " + ex.getMessage());
            if (ex.getSQLState().startsWith("23")) {
                System.err.println("Cannot delete destination due to existing hotels referencing it. " +
                        "Please delete associated hotels first.");
            }
        }
        return false;
    }
}