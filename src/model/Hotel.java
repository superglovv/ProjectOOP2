package model;

public class Hotel {
    private int id; // Adaugă acest câmp
    private String name;
    private int stars;
    private Destination location;

    // Constructor fără ID
    public Hotel(String name, int stars, Destination location) {
        this.name = name;
        this.stars = stars;
        this.location = location;
    }

    // Constructor cu ID
    public Hotel(int id, String name, int stars, Destination location) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.location = location;
    }

    // --- Getters ---
    public int getId() { // Adaugă acest getter
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    public Destination getLocation() {
        return location;
    }

    // --- Setters ---
    public void setId(int id) { // Adaugă acest setter
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setLocation(Destination location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stars=" + stars +
                ", location=" + location.getCity() + ", " + location.getCountry() +
                '}';
    }
}