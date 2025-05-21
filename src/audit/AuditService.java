package audit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private DateTimeFormatter formatter;

    private AuditService() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            if (new java.io.File(AUDIT_FILE).length() == 0) {
                writer.println("Action Name,Timestamp");
            }
        } catch (IOException e) {
            System.err.println("Error initializing audit file: " + e.getMessage());
        }
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static AuditService getInstance() {
        if (instance == null) {
            synchronized (AuditService.class) {
                if (instance == null) {
                    instance = new AuditService();
                }
            }
        }
        return instance;
    }

    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(formatter);
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) { // true for append mode
            writer.println(actionName + "," + timestamp);
            System.out.println("Audited action: " + actionName + " at " + timestamp);
        } catch (IOException e) {
            System.err.println("Error writing to audit file: " + e.getMessage());
        }
    }
}