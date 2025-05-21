package dao;

import model.ExtraServiceType;
import util.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ExtraServiceTypeDAO {

    private Map<String, Integer> typeNameToIdMap = new HashMap<>();
    private Map<Integer, ExtraServiceType> idToTypeMap = new HashMap<>();

    public ExtraServiceTypeDAO() {
        loadTypes();
    }

    private void loadTypes() {
        String SQL = "SELECT id, name FROM extra_service_types";
        try {
            Connection conn = DatabaseService.getInstance().getConnection(); // Obținem conexiunea
            try (PreparedStatement pstmt = conn.prepareStatement(SQL);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");

                    ExtraServiceType type = ExtraServiceType.valueOf(name);
                    typeNameToIdMap.put(name, id);
                    idToTypeMap.put(id, type);
                }
            }
        } catch (SQLException | IllegalArgumentException ex) {
            System.err.println("Error loading extra service types: " + ex.getMessage());
        }
    }

    public int getTypeByName(String name) {
        return typeNameToIdMap.getOrDefault(name, -1);
    }

    public ExtraServiceType getTypeById(int id) {
        return idToTypeMap.get(id);
    }
}