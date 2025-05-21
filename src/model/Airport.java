package model;

public class Airport {
    private int id; // Adaugă acest câmp
    private String code;
    private String name;
    private String city;

    // Constructor fără ID (pentru obiecte noi înainte de a fi salvate)
    public Airport(String code, String name, String city) {
        this.code = code;
        this.name = name;
        this.city = city;
    }

    // Constructor cu ID (pentru obiecte preluate din baza de date)
    public Airport(int id, String code, String name, String city) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.city = city;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}