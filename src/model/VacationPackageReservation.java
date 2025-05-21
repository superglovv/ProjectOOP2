package model;

import java.time.LocalDateTime;

public class VacationPackageReservation extends Reservation {
    private VacationPackage vacationPackage;

    public VacationPackageReservation(Client client, VacationPackage vacationPackage) {
        super(client); // Apelez constructorul clasei părinte (fără ID)
        this.vacationPackage = vacationPackage;
    }

    public VacationPackageReservation(int id, Client client, LocalDateTime reservationDate, VacationPackage vacationPackage) {
        super(id, client, reservationDate); // Apelez constructorul clasei părinte (cu ID)
        this.vacationPackage = vacationPackage;
    }

    public VacationPackage getVacationPackage() {
        return vacationPackage;
    }

    public void setVacationPackage(VacationPackage vacationPackage) {
        this.vacationPackage = vacationPackage;
    }

    @Override
    public String getReservationType() {
        return "Vacation Package";
    }

    @Override
    public String toString() {
        return "VacationPackageReservation{" +
                "id=" + getId() + // Folosește getId() de la clasa părinte
                ", client=" + getClient().getFirstName() + " " + getClient().getLastName() +
                ", reservationDate=" + getReservationDate() +
                ", vacationPackage=" + vacationPackage.getPackageName() +
                '}';
    }
}