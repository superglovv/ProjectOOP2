package dao;

import model.Destination;
import model.Hotel;
import util.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    private DestinationDAO destinationDAO = new DestinationDAO(); // Dependency

    private Connection getConnection() throws SQLException {
        return DatabaseService.getInstance().getConnection();
    }

    public int addHotel(Hotel hotel) {
        String SQL = "INSERT INTO hotels (name, stars, location_id) VALUES (?, ?, ?)";
        int id = -1;

        int locationId = destinationDAO.addDestination(hotel.getLocation());
        if (locationId == -1) {
            System.err.println("Failed to get/add hotel location. Cannot add hotel.");
            return -1;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, hotel.getName());
                pstmt.setInt(2, hotel.getStars());
                pstmt.setInt(3, locationId);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                            hotel.setId(id);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding hotel: " + ex.getMessage());
        }
        return id;
    }

    public Hotel getHotelById(int id) {
        String SQL = "SELECT id, name, stars, location_id FROM hotels WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Destination location = destinationDAO.getDestinationById(rs.getInt("location_id"));
                        if (location == null) {
                            System.err.println("Referenced destination not found for hotel ID: " + id);
                            return null;
                        }
                        return new Hotel(rs.getInt("id"), rs.getString("name"), rs.getInt("stars"), location);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting hotel by ID: " + ex.getMessage());
        }
        return null;
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String SQL = "SELECT id, name, stars, location_id FROM hotels";
        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {

                while (rs.next()) {
                    Destination location = destinationDAO.getDestinationById(rs.getInt("location_id"));
                    if (location != null) {
                        hotels.add(new Hotel(rs.getInt("id"), rs.getString("name"), rs.getInt("stars"), location));
                    } else {
                        System.err.println("Skipping hotel " + rs.getString("name") + " due to missing destination reference. (ID: " + rs.getInt("location_id") + ")");
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all hotels: " + ex.getMessage());
        }
        return hotels;
    }

    public boolean updateHotel(Hotel hotel) {
        String SQL = "UPDATE hotels SET name = ?, stars = ?, location_id = ? WHERE id = ?";

        int locationId = destinationDAO.addDestination(hotel.getLocation());
        if (locationId == -1) {
            System.err.println("Failed to get/add hotel location for update. Cannot update hotel.");
            return false;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, hotel.getName());
                pstmt.setInt(2, hotel.getStars());
                pstmt.setInt(3, locationId);
                pstmt.setInt(4, hotel.getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating hotel: " + ex.getMessage());
        }
        return false;
    }

    public boolean deleteHotel(int id) {

        String SQL = "DELETE FROM hotels WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting hotel: " + ex.getMessage());
            if (ex.getSQLState().startsWith("23")) {
                System.err.println("Cannot delete hotel (ID: " + id + ") due to existing vacation packages referencing it. " +
                        "Please delete associated packages first.");
            }
        }
        return false;
    }
}