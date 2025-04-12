package model;

public class Hotel {
    private String name;
    private int stars;
    private Destination location;

    public Hotel(String name, int stars, Destination location) {
        this.name = name;
        this.stars = stars;
        this.location = location;
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

    @Override
    public String toString() {
        return name + " (" + stars + "★), " + location;
    }
}
