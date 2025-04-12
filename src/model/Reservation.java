package model;

public abstract class Reservation {
    protected Client client;
    protected String type;

    public Reservation(Client client, String type) {
        this.client = client;
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public String getType() {
        return type;
    }

    public abstract void displayDetails();
}

