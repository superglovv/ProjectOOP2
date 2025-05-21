package dao;

import model.Client;
import util.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private Connection getConnection() throws SQLException {

        return DatabaseService.getInstance().getConnection();
    }

    public int addClient(Client client) {
        String SQL = "INSERT INTO clients (first_name, last_name, email) VALUES (?, ?, ?)";
        int id = -1;
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, client.getFirstName());
                pstmt.setString(2, client.getLastName());
                pstmt.setString(3, client.getEmail());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                            client.setId(id);
                        }
                    }
                }
            }
        } catch (SQLException ex) {

            if (ex.getSQLState().startsWith("23")) {
                System.out.println("Client with email '" + client.getEmail() + "' already exists. Retrieving existing ID.");
                Client existingClient = getClientByEmail(client.getEmail());
                if (existingClient != null) {
                    id = existingClient.getId();
                    client.setId(id);
                }
            } else {
                System.err.println("Error adding client: " + ex.getMessage());
            }
        }
        return id;
    }

    public Client getClientById(int id) {
        String SQL = "SELECT id, first_name, last_name, email FROM clients WHERE id = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Client(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting client by ID: " + ex.getMessage());
        }
        return null;
    }

    public Client getClientByEmail(String email) {
        String SQL = "SELECT id, first_name, last_name, email FROM clients WHERE email = ?";
        try {
            Connection conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Client(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting client by email: " + ex.getMessage());
        }
        return null;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String SQL = "SELECT id, first_name, last_name, email FROM clients";
        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {

                while (rs.next()) {
                    clients.add(new Client(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all clients: " + ex.getMessage());
        }
        return clients;
    }

    public boolean updateClient(Client client) {
        String SQL = "UPDATE clients SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
        try {
            Connection conn = getConnection(); // Obținem conexiunea
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setString(1, client.getFirstName());
                pstmt.setString(2, client.getLastName());
                pstmt.setString(3, client.getEmail());
                pstmt.setInt(4, client.getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error updating client: " + ex.getMessage());
        }
        return false;
    }

    public boolean deleteClient(int id) {
        String SQL = "DELETE FROM clients WHERE id = ?";
        try {
            Connection conn = getConnection(); // Obținem conexiunea
            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting client: " + ex.getMessage());
            if (ex.getSQLState().startsWith("23")) {
                System.err.println("Cannot delete client due to existing reservations referencing them. " +
                        "Please delete associated reservations first.");
            }
        }
        return false;
    }
}