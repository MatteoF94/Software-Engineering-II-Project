/**
 * Created by Matteo on 02/12/16.
 */
public class Reservation {
    private Car reservedCar;
    private User reservingUser;

    public Reservation(Car reservedCar, User reservingUser) {
        this.reservedCar = reservedCar;
        this.reservingUser = reservingUser;
    }

    public Car getCar() {
        String monitoredCarID = reservedCar.getSerialID();
        return new Car(monitoredCarID);
    }

    public User getUser() {
        String userID = reservingUser.getUserID();
        return new User("","",userID);
    }
}
