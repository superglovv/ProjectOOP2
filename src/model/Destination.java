package model;

public class Destination {
    private int id; // Adaugă acest câmp
    private String country;
    private String city;

    // Constructor fără ID
    public Destination(String country, String city) {
        this.country = country;
        this.city = city;
    }

    // Constructor cu ID
    public Destination(int id, String country, String city) {
        this.id = id;
        this.country = country;
        this.city = city;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}