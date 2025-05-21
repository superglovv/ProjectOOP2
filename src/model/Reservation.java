package model;

// Adaugă importul pentru util.DatabaseService.getInstance() dacă vei folosi conexiunea direct aici (un DAO este mai bun)
// Nu uita de importurile pentru LocalDate, LocalDateTime dacă le folosești pentru date
import java.time.LocalDateTime;

public abstract class Reservation {
    private int id; // Adaugă acest câmp
    private Client client;
    private LocalDateTime reservationDate; // Data la care s-a făcut rezervarea

    // Constructor fără ID
    public Reservation(Client client) {
        this.client = client;
        this.reservationDate = LocalDateTime.now(); // Setează data rezervării la momentul creării
    }

    // Constructor cu ID
    public Reservation(int id, Client client, LocalDateTime reservationDate) {
        this.id = id;
        this.client = client;
        this.reservationDate = reservationDate;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    // Metodă abstractă pentru a obține tipul rezervării (zbor sau pachet vacanță)
    public abstract String getReservationType();

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + client.getFirstName() + " " + client.getLastName() +
                ", reservationDate=" + reservationDate +
                '}';
    }
}