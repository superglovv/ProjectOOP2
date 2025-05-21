package dao;

import model.ExtraService;
import model.Flight;
import model.Hotel;
import model.VacationPackage;
import util.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacationPackageDAO {

    private HotelDAO hotelDAO = new HotelDAO();
    private FlightDAO flightDAO = new FlightDAO();
    private ExtraServiceDAO extraServiceDAO = new ExtraServiceDAO();

    private Connection getConnection() throws SQLException {
        return DatabaseService.getInstance().getConnection();
    }

    public int addVacationPackage(VacationPackage pkg) {
        String SQL = "INSERT INTO vacation_packages (package_name, hotel_id, flight_id, total_price) VALUES (?, ?, ?, ?)";
        int id = -1;

        int hotelId = hotelDAO.addHotel(pkg.getHotel());
        int flightId = flightDAO.addFlight(pkg.getFlight());

        if (hotelId == -1 || flightId == -1) {
            System.err.println("Failed to get/add hotel or flight for vacation package. Cannot add package.");
            return -1;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, pkg.getPackageName());
                pstmt.setInt(2, hotelId);
                pstmt.setInt(3, flightId);
                pstmt.setDouble(4, pkg.getTotalPrice());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                            pkg.setId(id);

                            if (pkg.getServices() != null && !pkg.getServices().isEmpty()) {
                                addVacationPackageServices(id, pkg.getServices());
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding vacation package: " + ex.getMessage());
        }
        return id;
    }

    private void addVacationPackageServices(int vacationPackageId, List<ExtraService> services) throws SQLException {
        String SQL = "INSERT INTO vacation_package_services (vacation_package_id, extra_service_id) VALUES (?, ?)";

        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            for (ExtraService service : services) {
                int serviceId = extraServiceDAO.addExtraService(service);
                if (serviceId != -1) {
                    pstmt.setInt(1, vacationPackageId);
                    pstmt.setInt(2, serviceId);
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }
    }

    public VacationPackage getVacationPackageById(int id) {
        String SQL = "SELECT id, package_name, hotel_id, flight_id, total_price FROM vacation_packages WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Hotel hotel = hotelDAO.getHotelById(rs.getInt("hotel_id"));
                        Flight flight = flightDAO.getFlightById(rs.getInt("flight_id"));

                        if (hotel == null || flight == null) {
                            System.err.println("Referenced hotel or flight not found for vacation package ID: " + id);
                            return null;
                        }

                        List<ExtraService> services = getVacationPackageServices(id);

                        return new VacationPackage(rs.getInt("id"), rs.getString("package_name"), hotel, flight, services, rs.getDouble("total_price"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting vacation package by ID: " + ex.getMessage());
        }
        return null;
    }

    private List<ExtraService> getVacationPackageServices(int vacationPackageId) {
        List<Integer> serviceIds = new ArrayList<>();
        String SQL = "SELECT extra_service_id FROM vacation_package_services WHERE vacation_package_id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, vacationPackageId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        serviceIds.add(rs.getInt("extra_service_id"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting extra service IDs for vacation package: " + ex.getMessage());
        }
        return extraServiceDAO.getExtraServicesByIds(serviceIds);
    }

    public List<VacationPackage> getAllVacationPackages() {
        List<VacationPackage> packages = new ArrayList<>();
        String SQL = "SELECT id, package_name, hotel_id, flight_id, total_price FROM vacation_packages";
        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {

                while (rs.next()) {
                    Hotel hotel = hotelDAO.getHotelById(rs.getInt("hotel_id"));
                    Flight flight = flightDAO.getFlightById(rs.getInt("flight_id"));
                    List<ExtraService> services = getVacationPackageServices(rs.getInt("id"));

                    if (hotel != null && flight != null) {
                        packages.add(new VacationPackage(rs.getInt("id"), rs.getString("package_name"), hotel, flight, services, rs.getDouble("total_price")));
                    } else {
                        System.err.println("Skipping vacation package " + rs.getString("package_name") + " (ID: " + rs.getInt("id") + ") due to missing hotel or flight reference.");
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all vacation packages: " + ex.getMessage());
        }
        return packages;
    }

    public boolean updateVacationPackage(VacationPackage pkg) {
        String SQL = "UPDATE vacation_packages SET package_name = ?, hotel_id = ?, flight_id = ?, total_price = ? WHERE id = ?";

        int hotelId = hotelDAO.addHotel(pkg.getHotel());
        int flightId = flightDAO.addFlight(pkg.getFlight());

        if (hotelId == -1 || flightId == -1) {
            System.err.println("Failed to get/add hotel or flight for vacation package update. Cannot update package.");
            return false;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, pkg.getPackageName());
                pstmt.setInt(2, hotelId);
                pstmt.setInt(3, flightId);
                pstmt.setDouble(4, pkg.getTotalPrice());
                pstmt.setInt(5, pkg.getId());

                boolean updated = pstmt.executeUpdate() > 0;
                if (updated) {
                    clearVacationPackageServices(pkg.getId());
                    if (pkg.getServices() != null && !pkg.getServices().isEmpty()) {
                        addVacationPackageServices(pkg.getId(), pkg.getServices());
                    }
                }
                return updated;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating vacation package: " + ex.getMessage());
        }
        return false;
    }

    private void clearVacationPackageServices(int vacationPackageId) throws SQLException {
        String SQL = "DELETE FROM vacation_package_services WHERE vacation_package_id = ?";

        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, vacationPackageId);
            pstmt.executeUpdate();
        }
    }

    public boolean deleteVacationPackage(int id) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            String deleteServicesSQL = "DELETE FROM vacation_package_services WHERE vacation_package_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteServicesSQL)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }

            String deletePackageSQL = "DELETE FROM vacation_packages WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deletePackageSQL)) {
                pstmt.setInt(1, id);
                boolean deleted = pstmt.executeUpdate() > 0;
                if (deleted) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting vacation package (ID: " + id + "): " + ex.getMessage());

            if (conn != null) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
        } finally {
             try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true); // Reset auto-commit mode
                }
            } catch (SQLException resetEx) {
                System.err.println("Error resetting auto-commit for connection: " + resetEx.getMessage());
            }
        }
        return false;
    }
}