package util;

import model.ExtraServiceType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
    private static DatabaseService instance;
    private Connection connection;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/travel_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Euvoifacebinelaexamene@24";

    private DatabaseService() {
        try {
            System.out.println("Attempting to load JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC driver loaded successfully.");

            System.out.println("Attempting to establish database connection...");
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database successfully.");

            System.out.println("Initiating table creation and lookup population...");
            createTablesAndPopulateLookups();
            System.out.println("Table creation and lookup population completed.");

        } catch (SQLException e) {
            System.err.println("SQL Exception during database connection or setup: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to connect or set up the database.", e);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found. Please ensure mysql-connector-java.jar is in your classpath.", e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during DatabaseService initialization: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error during database initialization.", e);
        }
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            synchronized (DatabaseService.class) {
                if (instance == null) {
                    instance = new DatabaseService();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTablesAndPopulateLookups() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("Creating tables if they do not exist...");

            System.out.println("- Creating 'clients' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS clients (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "first_name VARCHAR(50) NOT NULL," +
                    "last_name VARCHAR(50) NOT NULL" +
                    ");");
            System.out.println("  'clients' table created/verified.");

            System.out.println("- Creating 'airports' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS airports (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "code VARCHAR(10) UNIQUE NOT NULL," +
                    "name VARCHAR(100) NOT NULL," +
                    "city VARCHAR(50) NOT NULL" +
                    ");");
            System.out.println("  'airports' table created/verified.");

            System.out.println("- Creating 'destinations' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS destinations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "country VARCHAR(50) NOT NULL," +
                    "city VARCHAR(50) NOT NULL," +
                    "UNIQUE (country, city)" +
                    ");");
            System.out.println("  'destinations' table created/verified.");

            System.out.println("- Creating 'hotels' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS hotels (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "stars INT NOT NULL," +
                    "location_id INT NOT NULL," +
                    "FOREIGN KEY (location_id) REFERENCES destinations(id)" +
                    ");");
            System.out.println("  'hotels' table created/verified.");

            System.out.println("- Creating 'flights' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS flights (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "flight_code VARCHAR(20) UNIQUE NOT NULL," +
                    "departure_airport_id INT NOT NULL," +
                    "arrival_airport_id INT NOT NULL," +
                    "departure_date DATETIME NOT NULL," +
                    "arrival_date DATETIME NOT NULL," +
                    "FOREIGN KEY (departure_airport_id) REFERENCES airports(id)," +
                    "FOREIGN KEY (arrival_airport_id) REFERENCES airports(id)" +
                    ");");
            System.out.println("  'flights' table created/verified.");

            System.out.println("- Creating 'extra_service_types' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS extra_service_types (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) UNIQUE NOT NULL" +
                    ");");
            System.out.println("  'extra_service_types' table created/verified.");

            System.out.println("- Creating 'extra_services' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS extra_services (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "type_id INT NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "FOREIGN KEY (type_id) REFERENCES extra_service_types(id)" +
                    ");");
            System.out.println("  'extra_services' table created/verified.");

            System.out.println("- Creating 'vacation_packages' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS vacation_packages (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "package_name VARCHAR(100) NOT NULL," +
                    "hotel_id INT NOT NULL," +
                    "flight_id INT NOT NULL," +
                    "total_price DOUBLE NOT NULL," +
                    "FOREIGN KEY (hotel_id) REFERENCES hotels(id)," +
                    "FOREIGN KEY (flight_id) REFERENCES flights(id)" +
                    ");");
            System.out.println("  'vacation_packages' table created/verified.");

            System.out.println("- Creating 'vacation_package_services' junction table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS vacation_package_services (" +
                    "vacation_package_id INT NOT NULL," +
                    "extra_service_id INT NOT NULL," +
                    "PRIMARY KEY (vacation_package_id, extra_service_id)," +
                    "FOREIGN KEY (vacation_package_id) REFERENCES vacation_packages(id)," +
                    "FOREIGN KEY (extra_service_id) REFERENCES extra_services(id)" +
                    ");");
            System.out.println("  'vacation_package_services' table created/verified.");

            System.out.println("- Creating 'reservations' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "client_id INT NOT NULL," +
                    "type VARCHAR(50) NOT NULL," + // e.g., 'FLIGHT', 'VACATION_PACKAGE'
                    "FOREIGN KEY (client_id) REFERENCES clients(id)" +
                    ");");
            System.out.println("  'reservations' table created/verified.");

            System.out.println("- Creating 'flight_reservations' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS flight_reservations (" +
                    "reservation_id INT PRIMARY KEY," +
                    "flight_id INT NOT NULL," +
                    "FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE," + // Cascade delete for reservation
                    "FOREIGN KEY (flight_id) REFERENCES flights(id)" +
                    ");");
            System.out.println("  'flight_reservations' table created/verified.");

            System.out.println("- Creating 'vacation_package_reservations' table...");
            stmt.execute("CREATE TABLE IF NOT EXISTS vacation_package_reservations (" +
                    "reservation_id INT PRIMARY KEY," +
                    "vacation_package_id INT NOT NULL," +
                    "FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE," + // Cascade delete for reservation
                    "FOREIGN KEY (vacation_package_id) REFERENCES vacation_packages(id)" +
                    ");");
            System.out.println("  'vacation_package_reservations' table created/verified.");

            System.out.println("Checking if 'extra_service_types' table needs population...");
            if (!isTablePopulated("extra_service_types")) {
                for (ExtraServiceType type : ExtraServiceType.values()) {
                    System.out.println("  Inserting ExtraServiceType: " + type.name());
                    stmt.execute("INSERT INTO extra_service_types (name) VALUES ('" + type.name() + "');");
                }
                System.out.println("Populated 'extra_service_types' table successfully.");
            } else {
                System.out.println("  'extra_service_types' table already populated.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during table creation or population: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private boolean isTablePopulated(String tableName) throws SQLException {
        String SQL = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if table '" + tableName + "' is populated: " + e.getMessage());
            throw e;
        }
        return false;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}