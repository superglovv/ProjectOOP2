package dao;

import model.ExtraService;
import model.ExtraServiceType;
import util.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExtraServiceDAO {

    private ExtraServiceTypeDAO extraServiceTypeDAO = new ExtraServiceTypeDAO();

    private Connection getConnection() throws SQLException {
        return DatabaseService.getInstance().getConnection();
    }

    public int addExtraService(ExtraService service) {
        String SQL = "INSERT INTO extra_services (type_id, price) VALUES (?, ?)";
        int id = -1;
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setInt(1, extraServiceTypeDAO.getTypeByName(service.getType().name()));
                pstmt.setDouble(2, service.getPrice());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                            service.setId(id);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding extra service: " + ex.getMessage());
        }
        return id;
    }

    public ExtraService getExtraServiceById(int id) {
        String SQL = "SELECT id, type_id, price FROM extra_services WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        ExtraServiceType type = extraServiceTypeDAO.getTypeById(rs.getInt("type_id"));
                        if (type == null) {
                            System.err.println("Warning: ExtraServiceType with ID " + rs.getInt("type_id") + " not found for ExtraService ID " + id);

                        }
                        return new ExtraService(rs.getInt("id"), type, rs.getDouble("price"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting extra service by ID: " + ex.getMessage());
        }
        return null;
    }

    public List<ExtraService> getExtraServicesByIds(List<Integer> ids) {
        List<ExtraService> services = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return services;
        }

        StringBuilder SQL = new StringBuilder("SELECT id, type_id, price FROM extra_services WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            SQL.append("?");
            if (i < ids.size() - 1) {
                SQL.append(",");
            }
        }
        SQL.append(")");

        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL.toString())) {

                for (int i = 0; i < ids.size(); i++) {
                    pstmt.setInt(i + 1, ids.get(i));
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        ExtraServiceType type = extraServiceTypeDAO.getTypeById(rs.getInt("type_id"));
                        if (type == null) {
                            System.err.println("Warning: ExtraServiceType with ID " + rs.getInt("type_id") + " not found for ExtraService ID " + rs.getInt("id"));
                            continue;
                        }
                        services.add(new ExtraService(rs.getInt("id"), type, rs.getDouble("price")));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting extra services by IDs: " + ex.getMessage());
        }
        return services;
    }

    public boolean updateExtraService(ExtraService service) {
        String SQL = "UPDATE extra_services SET type_id = ?, price = ? WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, extraServiceTypeDAO.getTypeByName(service.getType().name()));
                pstmt.setDouble(2, service.getPrice());
                pstmt.setInt(3, service.getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating extra service: " + ex.getMessage());
        }
        return false;
    }

    public boolean deleteExtraService(int id) {
        String SQL = "DELETE FROM extra_services WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting extra service: " + ex.getMessage());

            if (ex.getSQLState().startsWith("23")) {
                System.err.println("Cannot delete extra service due to existing vacation packages referencing it. " +
                        "Please delete associated packages first.");
            }
        }
        return false;
    }
}