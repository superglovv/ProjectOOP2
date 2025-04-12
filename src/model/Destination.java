package model;

public class Destination {
    private String country;
    private String city;

    public Destination(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return city + ", " + country;
    }
}
