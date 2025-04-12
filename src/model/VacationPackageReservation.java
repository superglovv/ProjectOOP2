package model;

public class VacationPackageReservation extends Reservation {
    private VacationPackage vacationPackage;

    public VacationPackageReservation(Client client, VacationPackage vacationPackage) {
        super(client, "package");
        this.vacationPackage = vacationPackage;
    }

    public VacationPackage getVacationPackage() {
        return vacationPackage;
    }

    @Override
    public void displayDetails() {
        System.out.println("Vacation Package reservation for " + client.getFirstName() + " " + client.getLastName()+ ": " + vacationPackage);
    }
}
